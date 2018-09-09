package DiscordAPI.utils;

import java.util.ArrayList;
import java.util.Iterator;

public class JsonArray extends ArrayList<Json> {
    public JsonArray(String message) {
        message = message.substring(1, message.length() - 1);
        Iterator<String> iterator = Json.ConvertToMap.convertToList(message);
        while (iterator.hasNext()) {
            final String s = iterator.next();
            this.add(new Json(s));
        }
    }
}
