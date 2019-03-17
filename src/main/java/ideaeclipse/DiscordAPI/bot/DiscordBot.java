package ideaeclipse.DiscordAPI.bot;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import ideaeclipse.AsyncUtility.Async;
import ideaeclipse.DiscordAPI.bot.objects.channel.Field;
import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.channel.directMessage.CreateDMChannel;
import ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.CreateChannel;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPI.bot.objects.presence.UserStatus;
import ideaeclipse.DiscordAPI.bot.objects.presence.game.GameType;
import ideaeclipse.DiscordAPI.bot.objects.role.CreateRole;
import ideaeclipse.DiscordAPI.bot.objects.role.IRole;
import ideaeclipse.DiscordAPI.bot.objects.serverCommands.ConsoleCommands;
import ideaeclipse.DiscordAPI.bot.objects.user.CreateDiscordUser;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPI.utils.MultiKeyMap;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.interfaces.IHttpRequests;
import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.HttpEvent;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.RateLimitRecorder;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.RequestTypes;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.WebSocketEvent;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.JsonArray;
import ideaeclipse.customLogger.CustomLogger;
import ideaeclipse.customLogger.LoggerManager;
import ideaeclipse.customTerminal.CommandsClass;
import ideaeclipse.customTerminal.CustomTerminal;
import ideaeclipse.customTerminal.exceptions.ImproperCommandFormat;
import ideaeclipse.reflectionListener.ListenerManager;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * TODO: Custom terminal, Ensure optional params are 1 and 2 or don't load.
 * TODO: Custom terminal, Ensure return type is string
 * TODO: user params must be String or primitive
 * TODO: Embed footer validation
 * TODO: Scheduler that updates all data every hour, reset scheduler time on reconnection
 * TODO: Redo ratelimit recorder to work per end point, not just in general
 * <p>
 * This class is used to encapsulate all data surrounding the functionality of the discord bot
 * <p>
 * Statically this will load all desired properties, start a logger manager instance, event manager instance, store
 * if you want to query the last 100 messages from each text channel
 *
 * @author Idaeclipse
 * @see IDiscordBot
 */
public class DiscordBot extends IDiscordBot {
    //Imported
    private final RateLimitRecorder rateLimitRecorder;
    private final LoggerManager loggerManager;
    private final ListenerManager listenerManager;
    private final IHttpRequests requests;
    private final String commandPrefix;

    //Generated
    private final MultiKeyMap<Long, String, IRole> roles = new MultiKeyMap<>();
    private final MultiKeyMap<Long, String, IDiscordUser> users = new MultiKeyMap<>();
    private final MultiKeyMap<Long, String, IChannel> channels = new MultiKeyMap<>();
    private final CustomLogger logger;
    private Long guildId;
    private IDiscordUser user;
    private WebSocket socket;

    /**
     * Starts all necessary processes,
     * loads bots guild(s)
     * loads roles, bot user, users, channels, starts websocket connection
     */
    DiscordBot(final String token, final LoggerManager loggerManager, final ListenerManager listenerManager, final IHttpRequests requests, final String commandPrefix, final CommandsClass commandsClass, final CommandsClass dmClass, final String commandChannel, final String embedFooter) {
        this.rateLimitRecorder = new RateLimitRecorder(this);
        this.loggerManager = loggerManager;
        this.listenerManager = listenerManager;
        this.requests = requests;
        this.commandPrefix = commandPrefix;
        this.logger = new CustomLogger(this.getClass(), this.loggerManager);
        logger.debug("Registering default event listener");
        this.listenerManager.registerListener(new EventListener(this, commandPrefix, commandsClass, dmClass, embedFooter, commandChannel));
        logger.debug("Starting Console");

        //Console instance
        Async.blankThread(() -> {
            Thread.currentThread().setName("AdminConsole");
            CustomTerminal<IMessage> input = new CustomTerminal<>(this,this.commandPrefix, new ConsoleCommands(this), IMessage.class);
            Scanner scanner = new Scanner(System.in);
            while (true) {
                try {
                    String r = String.valueOf(input.handleInput(scanner.nextLine(), null)).replace("`", "");
                    if (!r.equals("null")) {
                        List<Field> fields = Field.parser(r);
                        if (fields.size() > 0) {
                            StringBuilder builder = new StringBuilder("\n");
                            for (Field field : fields) {
                                builder.append(field.getName()).append("\n");
                                builder.append("    ").append(field.getValue().replaceAll("\n", "\n    ")).append("\n");
                            }
                            if (builder.charAt(builder.length() - 1) == '\n')
                                builder.deleteCharAt(builder.length() - 1);

                            logger.info(String.valueOf(builder));
                        } else
                            logger.info(r);
                    } else
                        logger.error("Server Message system issue");
                } catch (ImproperCommandFormat e) {
                    logger.error(e.getMessage());
                }
            }
        });
        logger.debug("Console Started");

        //Loading all proprietary data
        try {
            logger.info("Finding bots guild id");
            JsonArray array = new JsonArray(String.valueOf(this.rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.GET, "/users/@me/guilds"))));
            switch (array.length()) {
                case 0:
                    logger.error("Your bot is not a member of any guild therefore can't load data. Please join a guild before launching the bot\n" +
                            "Go to https://discordapp.com/developers/applications/ and select your application go to the oauth tab hit scope \"bot\" and adjust permission accordingly\n" +
                            "You can then use the link provided to make your bot join the desired server");
                    break;
                case 1:
                    Json guild = new Json(String.valueOf(array.get(0)));
                    this.guildId = Long.parseUnsignedLong(String.valueOf(guild.get("id")));
                    logger.info("Bot is connected to: " + guild.get("name") + " with id: " + this.guildId);
                    break;
                default:
                    logger.error("This api doesn't support sharding so you must only have the bot connected to one guild \"server\" at a time, this is done for validity of command data");
                    System.exit(-1);
                    break;
            }

            logger.info("Found bots guild id");
            logger.info("Loading roles");
            for (Object object : new JsonArray(String.valueOf(this.rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.GET, "guilds/" + this.guildId + "/roles"))))) {
                Json json = new Json(String.valueOf(object));
                CreateRole role = Util.checkConstructor(CreateRole.class, json, this).getObject();
                logger.debug(String.valueOf(role.getRole()));
                roles.put(role.getRole().getId(), role.getRole().getName(), role.getRole());
            }
            logger.info("Roles loaded");
            logger.info("Loading Discord Bot User");
            Json discordBotJson = new Json(String.valueOf(this.rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.GET, "users/@me"))));
            if (discordBotJson.keySet().contains("id")) {
                discordBotJson = new Json(String.valueOf(this.rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.GET, "guilds/" + this.guildId + "/members/" + discordBotJson.get("id")))));
                CreateDiscordUser discordBotUser = Util.checkConstructor(CreateDiscordUser.class, discordBotJson, this).getObject();
                this.user = discordBotUser.getUser();
                logger.debug(String.valueOf(this.user));
                logger.info("Loaded Discord Bot User");
            }
            logger.info("Loading users");
            for (Object object : new JsonArray(String.valueOf(this.rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.GET, "guilds/" + this.guildId + "/members" + "?limit=1000"))))) {
                Json json = new Json(String.valueOf(object));
                CreateDiscordUser user = Util.checkConstructor(CreateDiscordUser.class, json, this).getObject();
                logger.debug(String.valueOf(user.getUser()));
                users.put(user.getUser().getId(), user.getUser().getUsername(), user.getUser());
            }
            logger.info("Users loaded");
            logger.info("Loading channels");
            for (Object object : new JsonArray(String.valueOf(this.rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.GET, "guilds/" + this.guildId + "/channels"))))) {
                Json json = new Json(String.valueOf(object));
                CreateChannel channel = Util.checkConstructor(CreateChannel.class, json, this).getObject();
                if (channel.getChannel() != null) {
                    logger.debug(String.valueOf(channel.getChannel()));
                    channels.put(channel.getChannel().getId(), channel.getChannel().getName(), channel.getChannel());
                }
            }
            logger.info("Channels loaded");
            try {
                logger.debug("Starting socket connection");
                this.socket = new Wss(this, token).getSocket();
            } catch (IOException |
                    WebSocketException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            logger.error("Could not load all proprietary data check https://status.discordapp.com/ for more info");
            System.exit(-1);
        }
    }

    /**
     * @return Logger Manager
     */
    public LoggerManager getLoggerManager() {
        return this.loggerManager;
    }

    /**
     * @return Event Listener Manager
     */
    public ListenerManager getListenerManager() {
        return this.listenerManager;
    }

    /**
     * @return rate limit recorder
     */
    public RateLimitRecorder getRateLimitRecorder() {
        return this.rateLimitRecorder;
    }

    /**
     * @return http requests
     */
    public IHttpRequests getRequests() {
        return this.requests;
    }

    @Override
    public IDiscordUser getBot() {
        return this.user;
    }

    @Override
    public Long getGuildId() {
        return this.guildId;
    }

    @Override
    public MultiKeyMap<Long, String, IDiscordUser> getUsers() {
        return this.users;
    }

    @Override
    public MultiKeyMap<Long, String, IChannel> getChannels() {
        return this.channels;
    }

    @Override
    public MultiKeyMap<Long, String, IRole> getRoles() {
        return this.roles;
    }

    @Override
    public void setStatus(final GameType type, final String message, final UserStatus status) {
        String payload = "{\"op\":3,\"d\":{\"since\":0,\"game\":{\"name\":\"?name\",\"type\":?type},\"afk\":false,\"status\":\"?status\"}}";
        payload = payload.replace("?name", message).replace("?type", String.valueOf(type.ordinal())).replace("?status", status.name());
        this.rateLimitRecorder.queue(new WebSocketEvent(this.socket, new Json(payload)));
    }

    @Override
    public IChannel createDmChannel(final IDiscordUser user) {
        String info = "{\"recipient_id\":?id}";
        info = info.replace("?id", String.valueOf(user.getId()));
        CreateDMChannel channel = Util.checkConstructor(CreateDMChannel.class, new Json(String.valueOf(this.rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.SENDJSON, "/users/@me/channels", new Json(info))))), this).getObject();
        this.getChannels().put(channel.getChannel().getId(), channel.getChannel().getName(), channel.getChannel());
        return channel.getChannel();
    }
}
