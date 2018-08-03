package DiscordAPI.WebSocket.Utils.Parsers;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DRole;
import DiscordAPI.Objects.DUser;
import DiscordAPI.WebSocket.Utils.DiscordUtils;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserP {
    private Long id;
    private DUser user;
    private DiscordBot bot;
    private JSONObject object;

    public UserP(Long id, DiscordBot DiscordBot) {
        this.id = id;
        this.bot = DiscordBot;
    }

    public UserP(JSONObject object, DiscordBot bot) {
        this.object = object;
        this.bot = bot;
    }

    public UserP logic() {
        if (object == null) {
            object = (JSONObject) bot.getRequests().get("guilds/" + bot.getGuildId() + "/members/" + id);
        }
        List<DRole> roles = new ArrayList<>();
        for (String s : (List<String>) object.get("roles")) {
            roles.add(DiscordUtils.Search.ROLES(bot.getRoles(), Long.parseUnsignedLong(s)));
        }
        Payloads.User u = null;
        if (object.get("user") != null) {
            u = DiscordUtils.Parser.convertToJSON((JSONObject) object.get("user"),Payloads.User.class);
        }
        user = new DUser(u.id, u.username, u.discriminator, roles);
        return this;
    }

    public DUser getUser() {
        return this.user;
    }
}
