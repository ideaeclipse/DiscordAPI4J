package DiscordAPI;

import DiscordAPI.Bot.Shards;
import DiscordAPI.Objects.DChannel;
import DiscordAPI.Objects.DRole;
import DiscordAPI.Objects.DUser;
import DiscordAPI.WebSocket.JsonData.Identity.IdentityObject;
import DiscordAPI.WebSocket.JsonData.PAYLOAD;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import DiscordAPI.WebSocket.Utils.DiscordUtils;
import DiscordAPI.WebSocket.Utils.Parsers.ChannelP;
import DiscordAPI.WebSocket.Utils.Parsers.RoleP;
import DiscordAPI.WebSocket.Utils.Parsers.UserP;
import DiscordAPI.WebSocket.Wss;
import DiscordAPI.listener.Dispatcher.TDispatcher;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiscordBot {
    DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private JSONObject identity;
    private TDispatcher dispatcher;
    private long guildId;
    private List<DChannel> channels;
    private List<DUser> users;
    private List<DRole> roles;
    private WebSocket socket;
    private Long id;

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
        logger.info("Connecting to WebSocket");
        try {
            socket = Wss.connect(this);
            getBotId();
            updateRoles();
            updateChannels();
            updateUsers();
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }

        return this;
    }

    public void updateChannels() {
        channels = new ArrayList<>();
        JSONArray array = (JSONArray) DiscordUtils.HttpRequests.get(DiscordUtils.DefaultLinks.GUILD + guildId + "/" + DiscordUtils.DefaultLinks.CHANNEL);
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
        JSONArray array = (JSONArray) DiscordUtils.HttpRequests.get(DiscordUtils.DefaultLinks.GUILD + guildId + DiscordUtils.DefaultLinks.MEMBER + "?limit=1000");
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            UserP userData = new UserP(object, this).logic();
            users.add(userData.getUser());
        }
    }

    private void updateRoles() {
        roles = new ArrayList<>();
        JSONArray array = (JSONArray) DiscordUtils.HttpRequests.get(DiscordUtils.DefaultLinks.GUILD + guildId + DiscordUtils.DefaultLinks.ROLE);
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            RoleP rd = new RoleP(object).logic();
            roles.add(rd.getRole());
        }
    }

    private void getBotId() {
        JSONObject object = (JSONObject) DiscordUtils.HttpRequests.get(DiscordUtils.DefaultLinks.USERME);
        id = Long.parseUnsignedLong(String.valueOf(object.get("id")));
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
        JSONObject object = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(DiscordUtils.BuildJSON.BuildJSON(PAYLOAD.values(), this)));
        object.put("op", 2);
        object.put("d", identity);
        return object;
    }

    public TDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public Long getId() {
        return id;
    }
}
