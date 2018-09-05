package DiscordAPI.objects;

import DiscordAPI.IPrivateBot;
import DiscordAPI.IDiscordBot;
import DiscordAPI.listener.genericListener.IDispatcher;
import DiscordAPI.objects.Interfaces.IChannel;
import DiscordAPI.objects.Interfaces.IDiscordUser;
import DiscordAPI.objects.Interfaces.IRole;
import DiscordAPI.objects.Interfaces.IUser;
import DiscordAPI.utils.*;
import DiscordAPI.utils.Properties;
import DiscordAPI.webSocket.TextOpCodes;
import DiscordAPI.webSocket.Wss;
import com.neovisionaries.ws.client.WebSocketException;

import java.io.IOException;
import java.util.*;

import static DiscordAPI.utils.DiscordUtils.DefaultLinks.*;
import static DiscordAPI.utils.RateLimitRecorder.QueueHandler.*;

/**
 * This is the main object used in this library, not visible to the developer
 * Uses IDiscordBot as it's Interface
 * To create a new instance of DiscordBot see DiscrdBotBuilder {@link DiscordBotBuilder}
 *
 * @author Ideaeclipse
 */
@SuppressWarnings("ALL")
class DiscordBot implements IDiscordBot, IPrivateBot {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final Properties properties;
    private TerminalManager terminalManager;
    private final Json identity;
    private final IDispatcher dispatcher;
    private final long guildId;
    private List<IChannel> channels;
    private List<IUser> users;
    private List<IRole> roles;
    private IDiscordUser user;
    private Wss textWss;

    /**
     * Called from {@link DiscordBotBuilder} {@link DiscordAPI.IDiscordBotBuilder}
     *
     * @param token   IPrivateBot token
     * @param guildID Guild id (Right click server and hit copy id)
     */
    DiscordBot(final String token, final long guildID) {
        properties = new Properties();
        try {
            properties.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DiscordUtils.DefaultLinks.bot = this;
        if (bot.getProperties().getProperty("debug").equals("true")) {
            logger.setLevel(DiscordLogger.Level.TRACE);
        }
        DiscordUtils.DefaultLinks.token = token;
        this.identity = buildIdentity();
        logger.info("Starting Rate Limit Monitor");
        DiscordUtils.DefaultLinks.rateLimitRecorder = new RateLimitRecorder();
        this.guildId = guildID;
        dispatcher = new IDispatcher();
        //audioManager = new AudioManager(this);
    }

    /**
     * @return Current AudioManager
     */

    public AudioManager getAudioManager() {
        return null;
    }

    /**
     * Creates the 'd' value of the identity payload
     *
     * @return Identity object
     */
    private Json buildIdentity() {
        Builder.Identity i = new Builder.Identity();

        i.token = token;
        i.properties = Builder.buildData(new Builder.Identity.Properties());

        Builder.Identity.Presence p = new Builder.Identity.Presence();
        p.game = Builder.buildData(new Builder.Identity.Presence.Game());

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
        roles = Async.queue(uRoles(), "RoleUpdate");
        Async.AsyncList list = new Async.AsyncList().add(uChannels()).add(uUsers()).add(uBotUser());
        List asyncList = Async.execute(list);
        channels = (List<IChannel>) asyncList.get(0);
        users = (List<IUser>) asyncList.get(1);
        List<DiscordUser> bot = (List<DiscordUser>) asyncList.get(2);
        user = bot.get(0);
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
                //audioManager.joinChannel(DiscordUtils.Search.VOICECHANNEL(getChannels(), "Bot"));
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
        return Builder.buildPayload(TextOpCodes.Identify, this.identity);
    }

    @Override
    public Properties getProperties() {
        return this.properties;
    }

    /**
     * @return Listener Dispatcher
     */
    @Override
    public IDispatcher getDispatcher() {
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
     * Creating dm channel with a user to search for a user use {@link DiscordAPI.utils.DiscordUtils.Search#USER(List, String)}
     *
     * @param user that you wish to create a dm channel with
     * @return returns said dm channel
     */
    @Override
    public IChannel createDmChannel(final IUser user) {
        Builder.CreateDmChannel cm = new Builder.CreateDmChannel();
        cm.recipient_id = user.getDiscordUser().getId();
        Channel.ChannelP parser = new Channel.ChannelP(new Json((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.sendJson, USERME + "/channels", new Json(Builder.buildData(cm)))))).logic();
        return parser.getChannel();
    }

    @Override
    public void updateRoles() {
        roles = Async.queue(uRoles(), "RoleUpdate");
    }

    @Override
    public void updateUsers() {
        users = Async.queue(uUsers(), "UserUpdate");
    }

    @Override
    public void updateChannels() {
        channels = Async.queue(uChannels(), "ChannelUpdate");
    }

    public Async.IU<List<IRole>> uRoles() {
        return () -> {
            List<IRole> roles = new ArrayList<>();
            logger.debug("Starting Role Update");
            JsonArray j = new JsonArray((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + guildId + ROLE)));
            for (Json o : j) {
                Role.RoleP rd = new Role.RoleP(bot, o).logic();
                roles.add(rd.getRole());
            }
            logger.info("Updated Roles");
            return roles;
        };
    }

    public Async.IU<List<IChannel>> uChannels() {
        return () -> {
            List<IChannel> channels = new LinkedList<>();
            logger.debug("Starting Channel Update");
            JsonArray array = new JsonArray((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + guildId + "/" + CHANNEL)));
            for (Json o : array) {
                if (Integer.parseInt(String.valueOf(o.get("type"))) == 0) {
                    Channel.ChannelP cd = new Channel.ChannelP(o).logic();
                    channels.add(cd.getChannel());
                } else if (Integer.parseInt(String.valueOf(o.get("type"))) == 2) {
                    VoiceChannel.ChannelP cd = new VoiceChannel.ChannelP(o).logic();
                    channels.add(cd.getChannel());
                }
            }
            logger.info("Updated Channel Listings");
            return channels;
        };
    }

    public Async.IU<List<IUser>> uUsers() {
        return () -> {
            List<IUser> users = new LinkedList<>();
            logger.debug("Starting User Update");
            JsonArray array = new JsonArray((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + guildId + MEMBER + "?limit=1000")));
            for (Json o : array) {
                User.ServerUniqueUserP userData = new User.ServerUniqueUserP(bot, o).logic();
                users.add(userData.getServerUniqueUser());
            }
            logger.info("Updated User Listings");
            return users;
        };
    }

    private Async.IU<List<IDiscordUser>> uBotUser() {
        return () -> {
            List<IDiscordUser> users = new LinkedList<>();
            logger.debug("Starting Bot User Update");
            Json object = new Json((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, USERME)));
            DiscordUser.UserP us = new DiscordUser.UserP(Long.parseUnsignedLong((String) object.get("id")), bot).logicId();
            users.add(us.getUser());
            logger.info("Updated Bot user");
            return users;
        };
    }

    /**
     * changes bot status, gametypes: {@link DiscordAPI.objects.Payloads.GameTypes}
     *
     * @param gameType GameType you wish to display
     * @param gameName String you want your message to be
     */
    @Override
    public void setStatus(final Payloads.GameTypes gameType, final String gameName) {
        Builder.Identity.Presence p = new Builder.Identity.Presence();
        Builder.Identity.Presence.Game g = new Builder.Identity.Presence.Game();
        g.name = gameName;
        g.type = gameType.ordinal();
        p.game = Builder.buildData(g);
        rateLimitRecorder.queue(new WebSocketEvent(this.textWss.getWebSocket(), Builder.buildPayload(TextOpCodes.Status_Update, Builder.buildData(p))));
    }
}
