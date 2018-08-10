package DiscordAPI.WebSocket.Utils.Parsers;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DRole;
import DiscordAPI.Objects.DUser;
import DiscordAPI.WebSocket.JsonData.Payloads;
import DiscordAPI.WebSocket.Utils.DiscordUtils;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import static DiscordAPI.WebSocket.Utils.DiscordUtils.DefaultLinks.*;

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
            object = (JSONObject) DiscordUtils.HttpRequests.get(GUILD + bot.getGuildId() + MEMBER + "/" + id);
        }
        List<DRole> roles = new ArrayList<>();
        for (String s : (List<String>) object.get("roles")) {
            roles.add(DiscordUtils.Search.ROLES(bot.getRoles(), Long.parseUnsignedLong(s)));
        }
        Payloads.User u = null;
        if (object.get("user") != null) {
            u = DiscordUtils.Parser.convertToJSON((JSONObject) object.get("user"), Payloads.User.class);
        }
        assert u != null;
        user = new DUser(u.id, u.username, u.discriminator, roles);
        return this;
    }

    public DUser getUser() {
        return this.user;
    }
}
