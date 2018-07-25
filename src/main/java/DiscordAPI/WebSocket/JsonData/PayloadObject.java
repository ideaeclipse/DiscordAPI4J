package DiscordAPI.WebSocket.JsonData;

import DiscordAPI.WebSocket.Utils.ConvertJSON;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PayloadObject {
    private JSONObject object;
    static final JSONParser parser = new JSONParser();

    public PayloadObject(String payload) {
        object = (JSONObject) ConvertJSON.convertToJSONOBJECT(payload);
    }

    public Object getValue(String value) {
        return object.get(value);
    }
}
