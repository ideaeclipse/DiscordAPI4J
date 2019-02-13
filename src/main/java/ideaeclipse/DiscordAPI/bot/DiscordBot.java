package ideaeclipse.DiscordAPI.bot;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import ideaeclipse.AsyncUtility.Async;
import ideaeclipse.CustomProperties.Properties;
import ideaeclipse.DiscordAPI.bot.objects.channel.Field;
import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.channel.directMessage.CreateDMChannel;
import ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.CreateChannel;
import ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.DeleteChannel;
import ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.UpdateChannel;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPI.bot.objects.message.MessageCreate;
import ideaeclipse.DiscordAPI.bot.objects.presence.PresenceUpdate;
import ideaeclipse.DiscordAPI.bot.objects.presence.UserStatus;
import ideaeclipse.DiscordAPI.bot.objects.presence.game.GameType;
import ideaeclipse.DiscordAPI.bot.objects.reaction.AddReaction;
import ideaeclipse.DiscordAPI.bot.objects.reaction.RemoveReaction;
import ideaeclipse.DiscordAPI.bot.objects.role.CreateRole;
import ideaeclipse.DiscordAPI.bot.objects.role.DeleteRole;
import ideaeclipse.DiscordAPI.bot.objects.role.IRole;
import ideaeclipse.DiscordAPI.bot.objects.role.UpdateRole;
import ideaeclipse.DiscordAPI.bot.objects.user.CreateDiscordUser;
import ideaeclipse.DiscordAPI.bot.objects.user.DeleteDiscordUser;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPI.bot.objects.user.UpdateDiscordUser;
import ideaeclipse.DiscordAPI.utils.MultiKeyMap;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.interfaces.IHttpRequests;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.HttpEvent;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.RateLimitRecorder;
import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.RequestTypes;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.WebSocketEvent;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.JsonArray;
import ideaeclipse.customLogger.CustomLogger;
import ideaeclipse.customLogger.Level;
import ideaeclipse.customLogger.LoggerManager;
import ideaeclipse.customTerminal.CustomTerminal;
import ideaeclipse.customTerminal.exceptions.ImproperCommandFormat;
import ideaeclipse.reflectionListener.ListenerManager;
import ideaeclipse.reflectionListener.annotations.CallableEvent;
import ideaeclipse.reflectionListener.parents.Listener;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * TODO: Load all channel types
 * <p>
 * This class is used to encapsulate all data surrounding the functionality of the discord bot
 * <p>
 * Statically this will load all desired properties, start a logger manager instance, event manager instance, store
 * if you want to query the last 100 messages from each text channel
 *
 * @author Idaeclipse
 * @see IDiscordBot
 * @see IDiscordBot
 */
public final class DiscordBot implements IDiscordBot {
    private static final Properties properties;
    private static final LoggerManager loggerManager;
    private static final ListenerManager eventManager;
    private static final boolean queryMessages;
    private final RateLimitRecorder rateLimitRecorder;
    private final IHttpRequests requests;
    private final MultiKeyMap<Long, String, IRole> roles = new MultiKeyMap<>();
    private final MultiKeyMap<Long, String, IDiscordUser> users = new MultiKeyMap<>();
    private final MultiKeyMap<Long, String, IChannel> channels = new MultiKeyMap<>();
    private Long guildId;
    private IDiscordUser user;
    private WebSocket socket;

    static {
        properties = new Properties(new String[]{"LoadChannelMessages", "CommandPrefix", "UseInstances", "InstanceCommands", "GenericCommands", "DmCommands", "CommandChannel", "debug"});
        try {
            properties.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        loggerManager = new LoggerManager("logs", Boolean.parseBoolean(properties.getProperty("debug")) ? Level.DEBUG : Level.INFO);
        eventManager = new ListenerManager();
        queryMessages = Boolean.parseBoolean(properties.getProperty("LoadChannelMessages"));
    }

    /**
     * Starts all necessary processes,
     * loads bots guild(s)
     * loads roles, bot user, users, channels, starts websocket connection
     *
     * @param token     login token
     * @param listeners list of {@link ideaeclipse.reflectionListener.parents.Event} to add to the {@link #eventManager}
     */
    public DiscordBot(final String token, final Listener... listeners) {
        CustomLogger logger = new CustomLogger(this.getClass(), loggerManager);
        logger.debug("Starting Rate Limit manager");
        this.rateLimitRecorder = new RateLimitRecorder(this);
        logger.debug("Starting Requests manager");
        this.requests = new Util.HttpRequests(token);
        logger.debug("Registering default event listener");
        eventManager.registerListener(new EventListener(this));
        logger.debug("Registering user defined event listeners if any");
        for (Listener listener : listeners) {
            logger.debug("Registering user defined event with name: " + listener.getClass().getSimpleName());
            eventManager.registerListener(listener);
        }
        logger.debug("Starting Console");
        IDiscordBot bot = this;
        Async.blankThread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setName("AdminConsole");
                CustomTerminal<IDiscordUser, IDiscordBot, IMessage> input = new CustomTerminal<>(properties.getProperty("CommandPrefix"), false, null, "ideaeclipse.DiscordAPI.bot.objects.serverCommands", bot, IMessage.class);
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    try {
                        String r = String.valueOf(input.handleInput(scanner.nextLine(), null, null)).replace("`", "");
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
            }
        });
        logger.debug("Console Started");
        try {
            logger.info("Finding bots guild id");
            JsonArray array = new JsonArray(String.valueOf(rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.GET, "/users/@me/guilds"))));
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
            for (Object object : new JsonArray(String.valueOf(rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.GET, "guilds/" + this.guildId + "/roles"))))) {
                Json json = new Json(String.valueOf(object));
                CreateRole role = Util.checkConstructor(CreateRole.class, json, this).getObject();
                logger.debug(String.valueOf(role.getRole()));
                roles.put(role.getRole().getId(), role.getRole().getName(), role.getRole());
            }
            logger.info("Roles loaded");
            logger.info("Loading Discord Bot User");
            Json discordBotJson = new Json(String.valueOf(rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.GET, "users/@me"))));
            if (discordBotJson.keySet().contains("id")) {
                discordBotJson = new Json(String.valueOf(rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.GET, "guilds/" + this.guildId + "/members/" + discordBotJson.get("id")))));
                CreateDiscordUser discordBotUser = Util.checkConstructor(CreateDiscordUser.class, discordBotJson, this).getObject();
                this.user = discordBotUser.getUser();
                logger.debug(String.valueOf(this.user));
                logger.info("Loaded Discord Bot User");
            }
            logger.info("Loading users");
            for (Object object : new JsonArray(String.valueOf(rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.GET, "guilds/" + this.guildId + "/members" + "?limit=1000"))))) {
                Json json = new Json(String.valueOf(object));
                CreateDiscordUser user = Util.checkConstructor(CreateDiscordUser.class, json, this).getObject();
                logger.debug(String.valueOf(user.getUser()));
                users.put(user.getUser().getId(), user.getUser().getUsername(), user.getUser());
            }
            logger.info("Users loaded");
            logger.info("Loading channels");
            for (Object object : new JsonArray(String.valueOf(rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.GET, "guilds/" + this.guildId + "/channels"))))) {
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


    @Override
    public ListenerManager getListenerManager() {
        return eventManager;
    }

    @Override
    public LoggerManager getLoggerManager() {
        return loggerManager;
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
    public Properties getProperties() {
        return properties;
    }

    @Override
    public boolean queryMessages() {
        return queryMessages;
    }

    @Override
    public RateLimitRecorder getRateLimitRecorder() {
        return this.rateLimitRecorder;
    }

    @Override
    public IHttpRequests getRequests() {
        return this.requests;
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
        rateLimitRecorder.queue(new WebSocketEvent(this.socket, new Json(payload)));
    }

    @Override
    public IChannel createDmChannel(final IDiscordUser user) {
        String info = "{\"recipient_id\":?id}";
        info = info.replace("?id", String.valueOf(user.getId()));
        CreateDMChannel channel = Util.checkConstructor(CreateDMChannel.class, new Json(String.valueOf(rateLimitRecorder.queue(new HttpEvent(this, RequestTypes.SENDJSON, "/users/@me/channels", new Json(info))))), this).getObject();
        this.getChannels().put(channel.getChannel().getId(), channel.getChannel().getName(), channel.getChannel());
        return channel.getChannel();
    }

    /**
     * This class is an extension of {@link Listener} and is used to output/log all possible events sent from the websocket
     * it is also used to handle message input for commands {@link #commandLogic(MessageCreate)}
     *
     * @author Ideaeclipse
     */
    @SuppressWarnings("All")
    private static class EventListener implements Listener {
        private final CustomLogger logger;
        private final IDiscordBot bot;
        private final CustomTerminal<IDiscordUser, IDiscordBot, IMessage> input;
        private final CustomTerminal<IDiscordUser, IDiscordBot, IMessage> dmInput;

        /**
         * Starts logger, starts command handler
         *
         * @param bot bot instance
         */
        private EventListener(final IDiscordBot bot) {
            this.bot = bot;
            this.logger = new CustomLogger(this.getClass(), bot.getLoggerManager());
            this.input = new CustomTerminal<>(this.bot.getProperties().getProperty("CommandPrefix"), Boolean.parseBoolean(this.bot.getProperties().getProperty("UseInstances")), this.bot.getProperties().getProperty("InstanceCommands"), this.bot.getProperties().getProperty("GenericCommands"), bot, IMessage.class);
            this.dmInput = new CustomTerminal<>(this.bot.getProperties().getProperty("CommandPrefix"), false, null, this.bot.getProperties().getProperty("DmCommands"), bot, IMessage.class);
        }

        /**
         * Called when {@link MessageCreate} payload is sent
         * Handles all command input
         * {@link CustomTerminal#handleInput(String, Object, Object)}
         *
         * @param create {@link ideaeclipse.reflectionListener.Event} all methods with this event type are called when {@link EventManager#callEvent(Event)} is called with that event type
         */
        @CallableEvent
        private void commandLogic(final MessageCreate create) {
            IMessage message = create.getMessage();
            if (message.getChannel().getType() != 1) {
                this.logger.info("ChannelMessageCreate: " + message);
                if (!message.getUser().getUsername().equals(this.bot.getBot().getUsername()) && (message.getChannel().equals(bot.getChannels().getByK2(this.bot.getProperties().getProperty("CommandChannel")))) && message.getContent().startsWith(this.input.getPrefix())) {
                    try {
                        String r = String.valueOf(this.input.handleInput(message.getContent(), message.getUser(), message));
                        if (!r.equals("null")) {
                            List<Field> fieldList = Field.parser(r);
                            if (fieldList.size() > 0)
                                message.getChannel().sendEmbed(fieldList);
                            else
                                message.getChannel().sendMessage(r);
                        }
                    } catch (ImproperCommandFormat e) {
                        message.getChannel().sendMessage(e.getMessage());
                    }
                }
            } else {
                this.logger.info("DmMessageCreate: " + message);
                if (!message.getUser().getUsername().equals(this.bot.getBot().getUsername())) {
                    if (message.getContent().startsWith(this.input.getPrefix())) {
                        try {
                            String r = String.valueOf(this.dmInput.handleInput(message.getContent(), message.getUser(), message));
                            if (!r.equals("null")) {
                                List<Field> fieldList = Field.parser(r);
                                if (fieldList.size() > 0)
                                    message.getChannel().sendEmbed(fieldList);
                                else
                                    message.getChannel().sendMessage(r);
                            }
                        } catch (ImproperCommandFormat e) {
                            message.getChannel().sendMessage(e.getMessage());
                        }
                    } else
                        message.getChannel().sendMessage("All messages sent through dm's are meant to be commands use " + this.dmInput.getPrefix() + "help for commands");
                }
            }
        }

        /**
         * Called when {@link PresenceUpdate} payload is sent
         * Prints the updated presence object
         *
         * @param update {@link ideaeclipse.reflectionListener.Event} all methods with this event type are called when {@link EventManager#callEvent(Event)} is called with that event type
         */
        @CallableEvent
        private void onPresenceUpadte(final PresenceUpdate update) {
            this.logger.info("Presence Update: " + update.getPresence());
        }

        /**
         * Called when {@link CreateRole} payload is sent
         * Prints the role name that was created
         *
         * @param role {@link ideaeclipse.reflectionListener.Event} all methods with this event type are called when {@link EventManager#callEvent(Event)} is called with that event type
         * @see Wss
         */
        @CallableEvent
        private void onRoleCreate(final CreateRole role) {
            this.logger.info("Role added: " + role.getRole().getName());
        }

        /**
         * Called when {@link UpdateRole} payload is sent
         * Prints the role name that was updated
         *
         * @param role {@link ideaeclipse.reflectionListener.Event} all methods with this event type are called when {@link EventManager#callEvent(Event)} is called with that event type
         * @see Wss
         */
        @CallableEvent
        private void onRoleUpdate(final UpdateRole role) {
            this.logger.info("Role Updated: " + role.getRole().getName());
        }

        /**
         * Called when {@link DeleteRole} payload is sent
         * Prints the role name that was deleted
         *
         * @param role {@link ideaeclipse.reflectionListener.Event} all methods with this event type are called when {@link EventManager#callEvent(Event)} is called with that event type
         * @see Wss
         */
        @CallableEvent
        private void onDeleteRole(final DeleteRole role) {
            this.logger.info("Role delete: " + role.getRole().getName());
        }

        /**
         * Called when {@link CreateChannel} payload is sent
         * Prints the channel name that was created
         *
         * @param channel {@link ideaeclipse.reflectionListener.Event} all methods with this event type are called when {@link EventManager#callEvent(Event)} is called with that event type
         * @see Wss
         */
        @CallableEvent
        private void onChannelCreate(final CreateChannel channel) {
            this.logger.info("Channel added: " + channel.getChannel().getName());
        }

        /**
         * Called when {@link UpdateChannel} payload is sent
         * Prints the channel name that was updated
         *
         * @param channel {@link ideaeclipse.reflectionListener.Event} all methods with this event type are called when {@link EventManager#callEvent(Event)} is called with that event type
         * @see Wss
         */
        @CallableEvent
        private void onChannelUpdate(final UpdateChannel channel) {
            this.logger.info("Channel updated: " + channel.getChannel().getName());
        }

        /**
         * Called when {@link DeleteChannel} payload is sent
         * Prints the channel name that was deleted
         *
         * @param channel {@link ideaeclipse.reflectionListener.Event} all methods with this event type are called when {@link EventManager#callEvent(Event)} is called with that event type
         * @see Wss
         */
        @CallableEvent
        private void onDeleteChannel(final DeleteChannel channel) {
            this.logger.info("Channel deleted: " + channel.getChannel().getName());
        }

        /**
         * Called when {@link CreateDiscordUser} payload is sent
         * Prints the user name that was added
         *
         * @param user {@link ideaeclipse.reflectionListener.Event} all methods with this event type are called when {@link EventManager#callEvent(Event)} is called with that event type
         * @see Wss
         */
        @CallableEvent
        private void onUserJoin(final CreateDiscordUser user) {
            this.logger.info("User added: " + user.getUser().getUsername());
        }

        /**
         * Called when {@link UpdateDiscordUser} payload is sent
         * Prints the user name that was updated
         *
         * @param user {@link ideaeclipse.reflectionListener.Event} all methods with this event type are called when {@link EventManager#callEvent(Event)} is called with that event type
         * @see Wss
         */
        @CallableEvent
        private void onUserUpdate(final UpdateDiscordUser user) {
            this.logger.info("User updated: " + user.getUser().getUsername());
        }

        /**
         * Called when {@link DeleteDiscordUser} payload is sent
         * Prints the user name that was deleted
         *
         * @param user {@link ideaeclipse.reflectionListener.Event} all methods with this event type are called when {@link EventManager#callEvent(Event)} is called with that event type
         * @see Wss
         */
        @CallableEvent
        private void onDeleteUser(final DeleteDiscordUser user) {
            this.logger.info("User deleted: " + user.getUser().getUsername());
        }

        /**
         * Called when {@link AddReaction} payload is sent
         * Prints the reaction object that was added
         *
         * @param user {@link ideaeclipse.reflectionListener.Event} all methods with this event type are called when {@link EventManager#callEvent(Event)} is called with that event type
         * @see Wss
         */
        @CallableEvent
        private void onAddReaction(final AddReaction reaction) {
            this.logger.info("Reaction added: " + reaction.getReaction());
        }

        /**
         * Called when {@link RemoveReaction} payload is sent
         * Prints the reaction object that was removed
         *
         * @param user {@link ideaeclipse.reflectionListener.Event} all methods with this event type are called when {@link EventManager#callEvent(Event)} is called with that event type
         * @see Wss
         */
        @CallableEvent
        private void onRemoveReaction(final RemoveReaction reaction) {
            this.logger.info("Reaction removed: " + reaction.getReaction());
        }
    }
}
