package DiscordAPI.WebSocket.Utils.Parsers;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DUser;
import org.json.simple.JSONObject;

public class UserData {
    private String id;
    private DUser user;
    private DiscordBot DiscordBot;
    private JSONObject object;

    public UserData(String id, DiscordBot DiscordBot) {
        this.id = id;
        this.DiscordBot = DiscordBot;
    }

    public UserData(JSONObject object) {
        this.object = object;
    }

    public UserData logic() {
        if (object == null) {
            object = (JSONObject) DiscordBot.getRequests().get("users/" + id);
        }
        if(object.get("user")!=null){
            object = (JSONObject) object.get("user");
        }
        user = new DUser(Long.parseLong(String.valueOf(object.get("id"))), String.valueOf(object.get("username")), Integer.parseInt(String.valueOf(object.get("discriminator"))));
        return this;
    }

    public DUser getUser() {
        return this.user;
    }
}
