package DiscordAPI.WebSocket.Utils.Parsers;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DRole;
import DiscordAPI.Objects.DUser;
import DiscordAPI.WebSocket.Utils.Search;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private String id;
    private DUser user;
    private DiscordBot bot;
    private JSONObject object;

    public UserData(String id, DiscordBot DiscordBot) {
        this.id = id;
        this.bot = DiscordBot;
    }

    public UserData(JSONObject object,DiscordBot bot) {
        this.object = object;
        this.bot = bot;
    }

    public UserData logic() {
        if (object == null) {
            object = (JSONObject) bot.getRequests().get("guilds/" + bot.getGuildId() + "/members/" + id);
        }
        List<DRole> roles = new ArrayList<>();
        for(String s: (List<String>) object.get("roles")){
            roles.add(Search.ROLES(bot.getRoles(),Long.parseUnsignedLong(s)));
        }
        if (object.get("user") != null) {
            object = (JSONObject) object.get("user");
        }
        user = new DUser(Long.parseLong(String.valueOf(object.get("id"))), String.valueOf(object.get("username")), Integer.parseInt(String.valueOf(object.get("discriminator"))),roles);
        return this;
    }

    public DUser getUser() {
        return this.user;
    }
}
