package DiscordAPI.objects;

import DiscordAPI.IDiscordBot;
import DiscordAPI.utils.RateLimitRecorder;
import DiscordAPI.webSocket.OpCodes;
import DiscordAPI.utils.DiscordLogger;
import DiscordAPI.utils.DiscordUtils;
import DiscordAPI.webSocket.Wss;
import DiscordAPI.listener.dispatcher.TDispatcher;
import com.neovisionaries.ws.client.WebSocketException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.*;

import static DiscordAPI.utils.DiscordUtils.DefaultLinks.*;
import static DiscordAPI.utils.RateLimitRecorder.QueueHandler.*;

class DiscordBot implements IDiscordBot {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final JSONObject identity;
    private final TDispatcher dispatcher;
    private final long guildId;
    private List<Channel> channels;
    private List<User> users;
    private List<Role> roles;
    private User user;
    private Wss textWss;

    DiscordBot(final String token, final long guildID) {
        DiscordUtils.DefaultLinks.token = token;
        this.identity = buildIdentity();
        logger.info("Starting Rate Limit Monitor");
        DiscordUtils.DefaultLinks.rateLimitRecorder = new RateLimitRecorder();
        this.guildId = guildID;
        dispatcher = new TDispatcher();
    }

    private JSONObject buildIdentity() {
        JSONObject object = new JSONObject();
        Builder.Identity i = new Builder.Identity();

        i.token = token;
        i.properties = Builder.buildData(new Builder.Identity.Properties());

        Builder.Identity.Presence p = new Builder.Identity.Presence();
        p.game = Builder.buildData(new Builder.Identity.Presence.Game());

        i.presence = Builder.buildData(p);

        return Builder.buildData(i);
    }

    @Override
    public long getGuildId() {
        return guildId;
    }

    @Override
    public IDiscordBot login() {
        try {
            updateRoles();
            getBot();
            updateChannels();
            updateUsers();
            logger.info("Connecting to webSocket");
            this.textWss = new Wss(this);
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }
        logger.info("DiscordBot " + user.getName() + " Started");
        return this;
    }

    @Override
    public void updateChannels() {
        channels = new ArrayList<>();
        JSONArray array = (JSONArray) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + guildId + "/" + CHANNEL));
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            if (Integer.parseInt(String.valueOf(object.get("type"))) == 0) {
                Channel.ChannelP cd = new Channel.ChannelP(object).logic();
                channels.add(cd.getChannel());
            }
        }
        logger.info("Updated Channel Listings");
    }

    private void updateUsers() {
        users = new ArrayList<>();
        JSONArray array = (JSONArray) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + guildId + MEMBER + "?limit=1000"));
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            User.UserP userData = new User.UserP(object, this).logic();
            users.add(userData.getUser());
        }
        logger.info("Updated User Listings");
    }

    private void updateRoles() {
        roles = new ArrayList<>();
        JSONArray array = (JSONArray) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, GUILD + guildId + ROLE));
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            Role.RoleP rd = new Role.RoleP(object).logic();
            roles.add(rd.getRole());
            //System.out.println(rd.getRole().getName());
            //System.out.println(DiscordUtils.PermissionId.convertPermissions(rd.getRole().getPermission()));
        }
        logger.info("Updated Guild Roles");
    }

    private void getBot() {
        JSONObject object = (JSONObject) rateLimitRecorder.queue(new HttpEvent(RequestTypes.get, USERME));
        User.UserP us = new User.UserP(Long.parseUnsignedLong(String.valueOf(object.get("id"))), this).logic();
        user = us.getUser();
        logger.info("Updated Bot user");
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
    public JSONObject getIdentity() {
        return Builder.buildPayload(OpCodes.Identify, this.identity);
    }

    @Override
    public TDispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Channel createDmChannel(final User user) {
        Builder.CreateDmChannel cm = new Builder.CreateDmChannel();
        cm.recipient_id = user.getId();
        Channel.ChannelP parser = new Channel.ChannelP((JSONObject) Objects.requireNonNull(rateLimitRecorder.queue(new HttpEvent(RequestTypes.sendJson, USERME + "/channels", Builder.buildData(cm))))).logic();
        return parser.getChannel();
    }

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
