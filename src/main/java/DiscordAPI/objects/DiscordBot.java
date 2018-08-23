package DiscordAPI.objects;

import DiscordAPI.IDiscordBot;
import DiscordAPI.utils.*;
import DiscordAPI.webSocket.OpCodes;
import DiscordAPI.webSocket.Wss;
import DiscordAPI.listener.dispatcher.TDispatcher;
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
class DiscordBot implements IDiscordBot {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final Json identity;
    private final TDispatcher dispatcher;
    private final long guildId;
    private final AudioManager audioManager;
    private List<Channel> channels;
    private List<VoiceChannel> voiceChannels;
    private List<User> users;
    private List<Role> roles;
    private DiscordUser user;
    private Wss textWss;

    /**
     * Called from {@link DiscordBotBuilder} {@link DiscordAPI.IDiscordBotBuilder}
     *
     * @param token   Bot token
     * @param guildID Guild id (Right click server and hit copy id)
     */
    DiscordBot(final String token, final long guildID) {
        DiscordUtils.DefaultLinks.bot = this;
        logger.setLevel(DiscordLogger.Level.TRACE);
        DiscordUtils.DefaultLinks.token = token;
        this.identity = buildIdentity();
        logger.info("Starting Rate Limit Monitor");
        DiscordUtils.DefaultLinks.rateLimitRecorder = new RateLimitRecorder();
        this.guildId = guildID;
        dispatcher = new TDispatcher();
        audioManager = new AudioManager(this);
    }

    /**
     * @return Current AudioManager
     */
    public AudioManager getAudioManager() {
        return this.audioManager;
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

    /**
     * login method, starts the websocket connection and loads all the roles,users,channels from the server
     * also queries the /users/@me data of your bot so the user can get the DiscordUser object that contains the bot info
     *
     * @return new instance of DiscordBot
     */
    @Override
    public IDiscordBot login() {
        Async async = new Async(this);
        roles = async.queue(new URoles(this, guildId));
        async.asyncQueue(new UChannels(guildId));
        async.asyncQueue(new UUsers(this, guildId));
        async.asyncQueue(new UBot(this));
        List list = async.execute();
        channels = (List<Channel>) list.get(0);
        users = (List<User>) list.get(1);
        List<DiscordUser> bot = (List<DiscordUser>) list.get(2);
        user = bot.get(0);
        for (Role r : roles) {
            logger.debug(r.toString());
        }
        for (Channel c : channels) {
            logger.debug(c.toString());
        }
        for (User u : users) {
            logger.debug(u.toString());
        }
        logger.debug(user.toString());
        try {
            logger.info("Connecting to webSocket");
            this.textWss = new Wss(this);
            synchronized (this.textWss.getLock()) {
                this.textWss.getLock().wait();
                logger.info("DiscordBot " + user.getName() + " Started");
                audioManager.joinChannel(DiscordUtils.Search.VOICECHANNEL(getChannels(), "Bot"));
            }
        } catch (IOException | WebSocketException | InterruptedException e) {
            e.printStackTrace();
        }

        return this;
    }

    /*
    channels -> Text Channels
    voiceChannels -> Voice Channels;
     */
    private static class UChannels implements Async.IU {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private final Long guildId;

        UChannels(final Long guildId) {
            logger.setLevel(DiscordLogger.Level.TRACE);
            this.guildId = guildId;
        }

        @Override
        public List update() {
            List<Channel> channels = new LinkedList<>();
            logger.debug("Starting Channel Update");
            JsonArray array = new JsonArray((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + guildId + "/" + CHANNEL)));
            for (Json o : array) {
                Json object = o;
                if (Integer.parseInt(String.valueOf(object.get("type"))) == 0) {
                    Channel.ChannelP cd = new Channel.ChannelP(object).logic();
                    channels.add(cd.getChannel());
                } else if (Integer.parseInt(String.valueOf(object.get("type"))) == 2) {
                    VoiceChannel.ChannelP cd = new VoiceChannel.ChannelP(object).logic();
                    channels.add(cd.getChannel());
                }
            }
            logger.info("Updated Channel Listings");
            return channels;
        }
    }

    private static class UUsers implements Async.IU {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private final Long guildId;
        private final IDiscordBot bot;

        private UUsers(final IDiscordBot bot, final Long guildId) {
            logger.setLevel(DiscordLogger.Level.TRACE);
            this.guildId = guildId;
            this.bot = bot;
        }

        @Override
        public List update() {
            List<User> users = new LinkedList<>();
            logger.debug("Starting User Update");
            JsonArray array = new JsonArray((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + guildId + MEMBER + "?limit=1000")));
            for (Json o : array) {
                User.ServerUniqueUserP userData = new User.ServerUniqueUserP(bot, o).logic();
                users.add(userData.getServerUniqueUser());
            }
            logger.info("Updated DiscordUser Listings");
            return users;
        }
    }

    private static class URoles implements Async.IU {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private final List<Role> roles;
        private final Long guildId;
        private final IDiscordBot bot;

        private URoles(final IDiscordBot bot, final Long guildId) {
            logger.setLevel(DiscordLogger.Level.TRACE);
            roles = new LinkedList<>();
            this.bot = bot;
            this.guildId = guildId;
        }

        @Override
        public List update() {
            logger.debug("Starting Role Update");
            JsonArray j = new JsonArray((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + guildId + ROLE)));
            for (Json o : j) {
                Role.RoleP rd = new Role.RoleP(bot, o).logic();
                roles.add(rd.getRole());
                //System.out.println(rd.getRole().getName());
                //System.out.println(DiscordUtils.PermissionId.convertPermissions(rd.getRole().getPermission()));
            }
            logger.info("Updated Guild Roles");
            return roles;
        }
    }

    private static class UBot implements Async.IU {
        private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
        private final IDiscordBot bot;

        private UBot(final IDiscordBot bot) {
            logger.setLevel(DiscordLogger.Level.TRACE);
            this.bot = bot;
        }

        @Override
        public List update() {
            List<DiscordUser> users = new LinkedList<>();
            logger.debug("Starting Bot User Update");
            Json object = new Json((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, USERME)));
            DiscordUser.UserP us = new DiscordUser.UserP(Long.parseUnsignedLong((String) object.get("id")), bot).logicId();
            users.add(us.getUser());
            logger.info("Updated Bot user");
            return users;
        }
    }

    @Override
    public List<Channel> getChannels() {
        return channels;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public Json getIdentity() {
        return Builder.buildPayload(OpCodes.Identify, this.identity);
    }

    /**
     * @return Listener Dispatcher
     */
    @Override
    public TDispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Override
    public DiscordUser getBotUser() {
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
    public Channel createDmChannel(final User user) {
        Builder.CreateDmChannel cm = new Builder.CreateDmChannel();
        cm.recipient_id = user.getDiscordUser().getId();
        Channel.ChannelP parser = new Channel.ChannelP(new Json((String) rateLimitRecorder.queue(new HttpEvent(RequestTypes.sendJson, USERME + "/channels", new Json(Builder.buildData(cm)))))).logic();
        return parser.getChannel();
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
        rateLimitRecorder.queue(new WebSocketEvent(this.textWss.getWebSocket(), Builder.buildPayload(OpCodes.Status_Update, Builder.buildData(p))));
    }
}
