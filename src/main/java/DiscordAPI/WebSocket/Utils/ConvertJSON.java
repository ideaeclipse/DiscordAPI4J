package DiscordAPI.WebSocket.Utils;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConvertJSON {
    public static Object convertToJSONOBJECT(String message) {
        JSONParser parser = new JSONParser();
        try {
            return parser.parse(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
