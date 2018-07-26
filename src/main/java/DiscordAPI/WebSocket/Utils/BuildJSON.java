package DiscordAPI.WebSocket.Utils;

import DiscordAPI.DiscordBot;
import DiscordAPI.WebSocket.JsonData.IJSONObject;
import DiscordAPI.WebSocket.JsonData.Identity.IDENTITY;
import org.json.simple.JSONObject;

public class BuildJSON {
    public static JSONObject BuildJSON(IJSONObject[] values, DiscordBot DiscordBot) {
        JSONObject object = new JSONObject();
        for (IJSONObject d : values) {
            if (d == IDENTITY.token) {
                object.put(d, DiscordBot.getToken());
            } else if (!d.getaClass().isEnum()) {
                object.put(d, d.getDefaultValue());
            } else if (d.getaClass().isEnum()) {
                object.put(d, BuildJSON((IJSONObject[]) d.getaClass().getEnumConstants(), DiscordBot));
            }
        }
        return object;
    }
}
