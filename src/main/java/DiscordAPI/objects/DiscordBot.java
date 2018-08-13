package DiscordAPI.objects;

import DiscordAPI.IDiscordBot;
import DiscordAPI.bot.Shards;
import DiscordAPI.utils.RateLimitRecorder;
import DiscordAPI.webSocket.jsonData.identity.IdentityObject;
import DiscordAPI.webSocket.jsonData.PAYLOAD;
import DiscordAPI.utils.DiscordLogger;
import DiscordAPI.utils.DiscordUtils;
import DiscordAPI.webSocket.Wss;
import DiscordAPI.listener.dispatcher.TDispatcher;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static DiscordAPI.utils.DiscordUtils.DefaultLinks.*;

class DiscordBot implements IDiscordBot {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final JSONObject identity;
    private final TDispatcher dispatcher;
    private final long guildId;
    private List<Channel> channels;
    private List<User> users;
    private List<Role> roles;
    private User user;

    DiscordBot(final String token, final long guildID) {
        DiscordUtils.DefaultLinks.token = token;
        logger.info("Starting Rate Limit Monitor");
        DiscordUtils.DefaultLinks.rateLimitRecorder = new RateLimitRecorder();
        this.guildId = guildID;
        Shards shards = new Shards();
        IdentityObject identityObject = new IdentityObject();
        identity = identityObject.getIdentity();
        identity.put("shard", shards.getShards());
        dispatcher = new TDispatcher();
    }

    @Override
    public long getGuildId() {
        return guildId;
    }

    @Override
    public IDiscordBot login() {
        logger.info("Connecting to webSocket");
        try {
            new Wss(this);
            updateRoles();
            Thread.sleep(1000);
            getBot();
            Thread.sleep(1000);
            updateChannels();
            Thread.sleep(1000);
            updateUsers();
        } catch (IOException | WebSocketException | InterruptedException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public void updateChannels() {
        channels = new ArrayList<>();
        JSONArray array = (JSONArray) DiscordUtils.HttpRequests.get(GUILD + guildId + "/" + CHANNEL);
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
        JSONArray array = (JSONArray) DiscordUtils.HttpRequests.get(GUILD + guildId + MEMBER + "?limit=1000");
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            User.UserP userData = new User.UserP(object, this).logic();
            users.add(userData.getUser());
        }
        logger.info("Updated User Listings");
    }

    private void updateRoles() {
        roles = new ArrayList<>();
        JSONArray array = (JSONArray) DiscordUtils.HttpRequests.get(GUILD + guildId + ROLE);
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
        JSONObject object = (JSONObject) DiscordUtils.HttpRequests.get(USERME);
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
        JSONObject object = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(DiscordUtils.BuildJSON.BuildJSON(PAYLOAD.values())));
        object.put("op", 2);
        object.put("d", identity);
        return object;
    }

    @Override
    public TDispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Override
    public User getUser() {
        return user;
    }
}
