package DiscordAPI.WebSocket.Utils.Parsers;

import DiscordAPI.Objects.DRole;
import DiscordAPI.WebSocket.Utils.DiscordUtils;
import org.json.simple.JSONObject;

public class RoleP {
    private JSONObject object;
    private DRole role;

    public RoleP(JSONObject object) {
        this.object = object;
    }

    public RoleP logic() {
        Payloads.Role r = DiscordUtils.Parser.convertToJSON(object, Payloads.Role.class);
        role = new DRole(r.id, r.name, r.color, r.position, r.permissions);
        return this;
    }

    public DRole getRole() {
        return role;
    }
}
