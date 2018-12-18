package ideaeclipse.DiscordAPI.objects;

import com.neovisionaries.ws.client.WebSocketException;
import ideaeclipse.AsyncUtility.Async;
import ideaeclipse.AsyncUtility.AsyncList;
import ideaeclipse.AsyncUtility.ForEachList;
import ideaeclipse.CustomProperties.Properties;
import ideaeclipse.DiscordAPI.IDiscordBot;
import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.objects.Interfaces.IChannel;
import ideaeclipse.DiscordAPI.objects.Interfaces.IRole;
import ideaeclipse.DiscordAPI.objects.Interfaces.IUser;
import ideaeclipse.DiscordAPI.utils.DiscordUtils;
import ideaeclipse.DiscordAPI.utils.RateLimitRecorder;
import ideaeclipse.DiscordAPI.webSocket.TextOpCodes;
import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.JsonUtilities.Builder;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.JsonUtilities.JsonArray;
import ideaeclipse.customLogger.CustomLogger;
import ideaeclipse.customLogger.Level;
import ideaeclipse.customLogger.LoggerManager;
import ideaeclipse.reflectionListener.EventManager;
import ideaeclipse.reflectionListener.Listener;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static ideaeclipse.DiscordAPI.utils.DiscordUtils.DefaultLinks.*;
import static ideaeclipse.DiscordAPI.utils.RateLimitRecorder.QueueHandler.*;

/**
 * This is the main object used in this library, not visible to the developer
 * Uses IDiscordBot as it's Interface
 * To create a new instance of DiscordBot see DiscrdBotBuilder {@link DiscordBotBuilder}
 *
 * @author Ideaeclipse
 */
@SuppressWarnings("ALL")
class DiscordBot implements IDiscordBot, IPrivateBot {
    private final LoggerManager loggerManager;
    private final CustomLogger logger;
    private final Properties properties;
    private TerminalManager terminalManager;
    private final Json identity;
    private final EventManager dispatcher;
    private final long guildId;
    private List<IChannel> channels;
    private List<IUser> users;
    private List<IRole> roles;
    private IDiscordUser user;
    private Wss textWss;

    /**
     * Called from {@link DiscordBotBuilder} {@link ideaeclipse.DiscordAPI.objects.DiscordBotBuilder}
     *
     * @param token   IPrivateBot token
     * @param guildID Guild id (Right click server and hit copy id)
     */
    DiscordBot(final String token, final Listener listener, final long guildID) {
        properties = new Properties(new String[]{"adminUser", "adminGroup", "commandsDirectory", "genericDirectory", "adminFileDir", "defaultFileDirectory", "useTerminal", "debug"});
        try {
            properties.start();
        } catch (IOException e) {
            System.exit(-1);
        }
        this.loggerManager = new LoggerManager(System.getProperty("user.dir") + "/logs/", getProperties().getProperty("debug").equals("true") ? Level.DEBUG : Level.INFO);
        this.logger = new CustomLogger(this.getClass(), loggerManager);
        DiscordUtils.DefaultLinks.bot = this;
        DiscordUtils.DefaultLinks.token = token;
        this.identity = buildIdentity();
        logger.info("Starting Rate Limit Monitor");
        DiscordUtils.DefaultLinks.rateLimitRecorder = new RateLimitRecorder();
        this.guildId = guildID;
        dispatcher = new EventManager();
        if (listener != null) {
            dispatcher.registerListener(listener);
        }
    }

    /**
     * @return Current AudioManager
     */
    @Deprecated
    public AudioManager getAudioManager() {
        return null;
    }

    /**
     * Creates the 'd' value of the identity payload
     *
     * @return Identity object
     */
    private Json buildIdentity() {
        BuilderObjects.Identity i = new BuilderObjects.Identity();

        i.token = token;
        i.properties = Builder.buildData(new BuilderObjects.Identity.Properties());

        BuilderObjects.Identity.Presence p = new BuilderObjects.Identity.Presence();
        p.game = Builder.buildData(new BuilderObjects.Identity.Presence.Game());

        i.presence = Builder.buildData(p);
        return new Json(Builder.buildData(i));
    }

    @Override
    public long getGuildId() {
        return guildId;
    }

    @Override
    public IDiscordBot getPublicBot() {
        return this;
    }

    /**
     * login method, starts the websocket connection and loads all the roles,users,channels from the server
     * also queries the /users/@me data of your bot so the user can get the DiscordUser object that contains the bot info
     *
     * @return new instance of DiscordBot
     */
    @Override
    public IDiscordBot login() {
        //Updates Roles
        Async.queue(uRoles(), "RoleUpdate").ifPresent(o -> this.roles = o);
        AsyncList list = new ForEachList().add(uChannels()).add(uUsers()).add(uBotUser());
        Optional<List> optionalList = list.execute();
        List<Object> asyncList = optionalList.get();
        this.channels = (List<IChannel>) asyncList.get(0);
        this.users = (List<IUser>) asyncList.get(1);
        List<IDiscordUser> temp = (List<IDiscordUser>) asyncList.get(2);
        this.user = temp.get(0);
        for (IRole r : roles) {
            logger.debug(r.toString());
        }
        for (IChannel c : channels) {
            logger.debug(c.toString());
        }
        for (IUser u : users) {
            logger.debug(u.toString());
        }
        logger.debug(user.toString());

        try {
            logger.info("Connecting to webSocket");
            this.textWss = new Wss(this);
            synchronized (this.textWss.getLock()) {
                this.textWss.getLock().wait();
                logger.info("DiscordBot " + user.getName() + " Started");
                if (getProperties().getProperty("useTerminal").equals("true")) {
                    terminalManager = new TerminalManager(this);
                }
            }
        } catch (IOException | WebSocketException | InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public List<IChannel> getChannels() {
        return channels;
    }

    @Override
    public List<IUser> getUsers() {
        return users;
    }

    @Override
    public List<IRole> getRoles() {
        return roles;
    }

    @Override
    public Json getIdentity() {
        Json json = new Json();
        json.put("op",TextOpCodes.Identify.ordinal());
        json.put("d",new JSONObject(this.identity.toString()));
        return json;
    }

    @Override
    public Properties getProperties() {
        return this.properties;
    }

    @Override
    public LoggerManager getLoggerManager() {
        return this.loggerManager;
    }

    /**
     * @return Listener Dispatcher
     */
    @Override
    public EventManager getDispatcher() {
        return this.dispatcher;
    }

    @Override
    public IDiscordUser getBotUser() {
        return user;
    }

    public Wss getTextWss() {
        return textWss;
    }


    /**
     * Creating dm channel with a user to search for a user use {@link ideaeclipse.DiscordAPI.utils.DiscordUtils.Search#USER(List, String)}
     *
     * @param user that you wish to create a dm channel with
     * @return returns said dm channel
     */
    @Override
    public IChannel createDmChannel(final IUser user) {
        BuilderObjects.CreateDmChannel cm = new BuilderObjects.CreateDmChannel();
        cm.recipient_id = user.getDiscordUser().getId();
        Channel.ChannelP parser = new Channel.ChannelP(new Json((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.sendJson, USERME + "/channels", new Json(Builder.buildData(cm)))))).logic();
        return parser.getChannel();
    }

    @Override
    public void updateRoles() {
        Async.queue(uRoles(), "RoleUpdate").ifPresent(o -> this.roles = o);
    }

    @Override
    public void updateUsers() {
        Async.queue(uUsers(), "UserUpdate").ifPresent(o -> this.users = o);
    }

    @Override
    public void updateChannels() {
        Async.queue(uChannels(), "ChannelUpdate").ifPresent(o -> this.channels = o);
    }

    public Async.IU<List<IRole>> uRoles() {
        return x -> {
            List<IRole> roles = new ArrayList<>();
            logger.debug("Starting Role Update");
            JsonArray j = new JsonArray((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + guildId + ROLE)));
            for (Json o : j) {
                Role.RoleP rd = new Role.RoleP(bot, o).logic();
                roles.add(rd.getRole());
            }
            logger.info("Updated Roles");
            return Optional.of(roles);
        };
    }

    public Async.IU<List<IChannel>> uChannels() {
        return x -> {
            List<IChannel> channels = new LinkedList<>();
            logger.debug("Starting Channel Update");
            JsonArray array = new JsonArray((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + guildId + "/" + CHANNEL)));
            for (Json o : array) {
                if (!String.valueOf(o.get("type")).equals("null")) {
                    if (Integer.parseInt(String.valueOf(o.get("type"))) == 0) {
                        Channel.ChannelP cd = new Channel.ChannelP(o).logic();
                        channels.add(cd.getChannel());
                    } else if (Integer.parseInt(String.valueOf(o.get("type"))) == 2) {
                        VoiceChannel.ChannelP cd = new VoiceChannel.ChannelP(o).logic();
                        channels.add(cd.getChannel());
                    }
                }
            }
            logger.info("Updated Channel Listings");
            return Optional.of(channels);
        };
    }

    public Async.IU<List<IUser>> uUsers() {
        return x -> {
            List<IUser> users = new LinkedList<>();
            logger.debug("Starting User Update");
            JsonArray array = new JsonArray((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + guildId + MEMBER + "?limit=1000")));
            for (Json o : array) {
                User.ServerUniqueUserP userData = new User.ServerUniqueUserP(bot, o).logic();
                users.add(userData.getServerUniqueUser());
            }
            logger.info("Updated User Listings");
            return Optional.of(users);
        };
    }

    private Async.IU<List<IDiscordUser>> uBotUser() {
        return x -> {
            List<IDiscordUser> users = new LinkedList<>();
            logger.debug("Starting Bot User Update");
            Json object = new Json((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, USERME)));
            DiscordUser.UserP us = new DiscordUser.UserP(Long.parseUnsignedLong((String) object.get("id")), bot).logicId();
            users.add(us.getUser());
            logger.info("Updated Bot user");
            return Optional.of(users);
        };
    }

    /**
     * changes bot status, gametypes: {@link ideaeclipse.DiscordAPI.objects.Payloads.GameTypes}
     *
     * @param gameType GameType you wish to display
     * @param gameName String you want your message to be
     */
    @Override
    public void setStatus(final Payloads.GameTypes gameType, final String gameName) {
        BuilderObjects.Identity.Presence p = new BuilderObjects.Identity.Presence();
        BuilderObjects.Identity.Presence.Game g = new BuilderObjects.Identity.Presence.Game();
        g.name = gameName;
        g.type = gameType.ordinal();
        p.game = Builder.buildData(g);
        rateLimitRecorder.queue(new WebSocketEvent(this.textWss.getWebSocket(), Builder.buildPayload(TextOpCodes.Status_Update.ordinal(), Builder.buildData(p))));
    }
}
