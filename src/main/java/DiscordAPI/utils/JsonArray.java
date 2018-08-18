package DiscordAPI.utils;

import java.util.ArrayList;

public class JsonArray extends ArrayList<Json> {
    public JsonArray(String message) {
        message = message.substring(1, message.length() - 1);
        for (String s : Json.ConvertToMap.convertToList(message)) {
            this.add(new Json(s));
        }
    }
}
