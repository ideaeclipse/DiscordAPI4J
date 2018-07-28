package DiscordAPI.WebSocket.Voice.Ready;

import DiscordAPI.WebSocket.JsonData.IJSONObject;
import DiscordAPI.WebSocket.JsonData.OpCodes;
import DiscordAPI.WebSocket.Utils.PayloadObject;
import DiscordAPI.WebSocket.Voice.VoiceOpCodes;
import DiscordAPI.WebSocket.Voice.VoiceStateUpdate.VSU;
import org.json.simple.JSONObject;

import java.util.Arrays;

public class ReadyObject {
    private JSONObject payload;
    private JSONObject object;
    private VoiceOpCodes opcode;
    private PayloadObject payloadObject;
    private String endpoint;
    private int port;

    public ReadyObject(JSONObject object, VoiceOpCodes opcode, String endpoint, int port) {
        payload = new JSONObject();
        this.object = object;
        this.opcode = opcode;
        this.endpoint = endpoint;
        this.port = port;
    }

    public JSONObject logic(IJSONObject[] values) {
        JSONObject object = new JSONObject();
        System.out.println(Arrays.toString(values));
        for (IJSONObject d : values) {
            if (d.equals(DATA.address)) {
                object.put(d, this.endpoint);
            } else if (d.equals(DATA.port)) {
                object.put(d, this.port);
            } else if (!d.getaClass().isEnum()) {
                object.put(d, d.getDefaultValue());
            } else if (d.getaClass().isEnum()) {
                object.put(d, logic((IJSONObject[]) d.getaClass().getEnumConstants()));
            }
        }
        return object;
    }

    public JSONObject getPayload(JSONObject d) {
        payloadObject = new PayloadObject(opcode, d);
        return payloadObject.getPayload();
    }
}
