package DiscordAPI;

import DiscordAPI.bot.Shards;
import DiscordAPI.objects.DChannel;
import DiscordAPI.objects.DRole;
import DiscordAPI.objects.DUser;
import DiscordAPI.webSocket.jsonData.Payloads;
import DiscordAPI.webSocket.jsonData.identity.IdentityObject;
import DiscordAPI.webSocket.jsonData.PAYLOAD;
import DiscordAPI.webSocket.utils.DiscordLogger;
import DiscordAPI.webSocket.utils.DiscordUtils;
import DiscordAPI.webSocket.utils.parsers.ChannelP;
import DiscordAPI.webSocket.utils.parsers.RoleP;
import DiscordAPI.webSocket.utils.parsers.UserP;
import DiscordAPI.webSocket.Wss;
import DiscordAPI.listener.dispatcher.TDispatcher;
import DiscordAPI.webSocket.utils.parsers.permissions.Permissions;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static DiscordAPI.webSocket.utils.DiscordUtils.DefaultLinks.*;

public class DiscordBot {
    DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private JSONObject identity;
    private TDispatcher dispatcher;
    private long guildId;
    private List<DChannel> channels;
    private List<DUser> users;
    private List<DRole> roles;
    private DUser user;

    public DiscordBot(String token, long guildID) {
        DiscordUtils.DefaultLinks.token = token;
        this.guildId = guildID;
        Shards shards = new Shards();
        IdentityObject identityObject = new IdentityObject(this);
        identity = identityObject.getIdentity();
        identity.put("shard", shards.getShards());
        dispatcher = new TDispatcher(this);
    }

    public long getGuildId() {
        return guildId;
    }

    public DiscordBot login() {
        logger.info("Connecting to webSocket");
        try {
            WebSocket socket = Wss.connect(this);
            updateRoles();
            getBot();
            updateChannels();
            updateUsers();
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }

        return this;
    }

    public void updateChannels() {
        channels = new ArrayList<>();
        JSONArray array = (JSONArray) DiscordUtils.HttpRequests.get(GUILD + guildId + "/" + CHANNEL);
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            if (Integer.parseInt(String.valueOf(object.get("type"))) == 0) {
                ChannelP cd = new ChannelP(object).logic();
                channels.add(cd.getChannel());
            }
        }
    }

    private void updateUsers() {
        users = new ArrayList<>();
        JSONArray array = (JSONArray) DiscordUtils.HttpRequests.get(GUILD + guildId + MEMBER + "?limit=1000");
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            UserP userData = new UserP(object, this).logic();
            users.add(userData.getUser());
        }
    }

    private void updateRoles() {
        roles = new ArrayList<>();
        JSONArray array = (JSONArray) DiscordUtils.HttpRequests.get(GUILD + guildId + ROLE);
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            RoleP rd = new RoleP(object).logic();
            roles.add(rd.getRole());
            System.out.println(rd.getRole().getName());
            System.out.println(DiscordUtils.PermissionId.convertPermissions(rd.getRole().getPermission()));
        }
    }
    private void getBot() {
        JSONObject object = (JSONObject) DiscordUtils.HttpRequests.get(USERME);
        Payloads.User u = DiscordUtils.Parser.convertToJSON(object,Payloads.User.class);
        UserP us = new UserP(u.id,this).logic();
        user = us.getUser();
    }

    public List<DChannel> getChannels() {
        return channels;
    }

    public List<DUser> getUsers() {
        return users;
    }

    public List<DRole> getRoles() {
        return roles;
    }

    public JSONObject getIdentity() {
        JSONObject object = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(DiscordUtils.BuildJSON.BuildJSON(PAYLOAD.values())));
        object.put("op", 2);
        object.put("d", identity);
        return object;
    }

    public TDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public DUser getUser() {
        return user;
    }
}
