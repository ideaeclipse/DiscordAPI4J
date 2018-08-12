package DiscordAPI.webSocket.utils.parsers;

import DiscordAPI.DiscordBot;
import DiscordAPI.objects.DRole;
import DiscordAPI.objects.DUser;
import DiscordAPI.webSocket.jsonData.Payloads;
import DiscordAPI.webSocket.utils.DiscordUtils;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static DiscordAPI.webSocket.utils.DiscordUtils.DefaultLinks.*;

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
            DRole role = DiscordUtils.Search.ROLES(bot.getRoles(), Long.parseUnsignedLong(s));
            if (role != null) {
                roles.add(role);
            }
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
