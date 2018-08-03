package DiscordAPI.WebSocket.Utils.Parsers;

import DiscordAPI.Objects.DRole;
import org.json.simple.JSONObject;

public class RoleP {
    private JSONObject object;
    private DRole role;
    public RoleP(JSONObject object){
        this.object = object;
    }
    public RoleP logic(){
        role = new DRole(Long.parseUnsignedLong(String.valueOf(object.get("id"))),String.valueOf(object.get("name")),
                Integer.parseInt(String.valueOf(object.get("color"))),Integer.parseInt(String.valueOf(object.get("position"))),
                Long.parseUnsignedLong(String.valueOf(object.get("permissions"))));
        return this;
    }

    public DRole getRole() {
        return role;
    }
}
