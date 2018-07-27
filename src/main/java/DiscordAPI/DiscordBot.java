package DiscordAPI;

import DiscordAPI.Bot.Shards;
import DiscordAPI.HttpApi.HttpRequests;
import DiscordAPI.Objects.Audio.AudioManager;
import DiscordAPI.Objects.DChannel;
import DiscordAPI.Objects.DRole;
import DiscordAPI.Objects.DUser;
import DiscordAPI.WebSocket.JsonData.Identity.IdentityObject;
import DiscordAPI.WebSocket.JsonData.PAYLOAD;
import DiscordAPI.WebSocket.Utils.BuildJSON;
import DiscordAPI.WebSocket.Utils.ConvertJSON;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import DiscordAPI.WebSocket.Utils.Parsers.ChannelData;
import DiscordAPI.WebSocket.Utils.Parsers.RoleData;
import DiscordAPI.WebSocket.Utils.Parsers.UserData;
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
    private String token;
    private JSONObject identity;
    private HttpRequests requests;
    private TDispatcher dispatcher;
    private long guildId;
    private List<DChannel> channels;
    private List<DUser> users;
    private List<DRole> roles;
    private WebSocket socket;
    private AudioManager audioManager;
    private Long id;

    public DiscordBot(String token, long guildID) {
        this.token = token;
        this.guildId = guildID;
        Shards shards = new Shards();
        IdentityObject identityObject = new IdentityObject(this);
        identity = identityObject.getIdentity();
        identity.put("shard", shards.getShards());
        requests = new HttpRequests(this);
        dispatcher = new TDispatcher(this);
    }

    public long getGuildId() {
        return guildId;
    }

    public DiscordBot login() {
        try {
            logger.info("Connecting to WebSocket");
            audioManager = new AudioManager(this,Wss.connect(this),guildId);
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
        JSONArray array = (JSONArray) getRequests().get("guilds/" + guildId + "/channels");
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            if (Integer.parseInt(String.valueOf(object.get("type"))) == 0) {
                ChannelData cd = new ChannelData(object).logic();
                channels.add(cd.getChannel());
            }
        }
    }

    private void updateUsers() {
        users = new ArrayList<>();
        JSONArray array = (JSONArray) getRequests().get("guilds/" + guildId + "/members?limit=1000");
        for (Object o : array) {
            JSONObject object = (JSONObject) o;
            UserData userData = new UserData(object,this).logic();
            users.add(userData.getUser());
        }
    }
    private void updateRoles(){
        roles = new ArrayList<>();
        JSONArray array = (JSONArray) getRequests().get("guilds/" + guildId + "/roles");
        for(Object o : array){
            JSONObject object = (JSONObject) o;
            RoleData rd = new RoleData(object).logic();
            roles.add(rd.getRole());
        }
    }
    private void getBotId(){
        JSONObject object = (JSONObject) getRequests().get("users/@me");
        id = Long.parseUnsignedLong(String.valueOf(object.get("id")));
    }

    public AudioManager getAudioManager() {
        return audioManager;
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

    public String getToken() {
        return token;
    }

    public JSONObject getIdentity() {
        JSONObject object = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(BuildJSON.BuildJSON(PAYLOAD.values(), this)));
        object.put("op", 2);
        object.put("d", identity);
        return object;
    }

    public HttpRequests getRequests() {
        return requests;
    }

    public TDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public Long getId() {
        return id;
    }
}
