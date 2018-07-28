package DiscordAPI.WebSocket.Utils;

import DiscordAPI.WebSocket.JsonData.OpCodes;
import DiscordAPI.WebSocket.Voice.VoiceOpCodes;
import org.json.simple.JSONObject;

public class PayloadObject {
    private JSONObject payload;

    public PayloadObject(OpCodes opCode, Object object) {
        payload = new JSONObject();
        payload.put("op", opCode.getOpcode());
        payload.put("d", object);
    }

    public PayloadObject(VoiceOpCodes opCode, Object object) {
        payload = new JSONObject();
        payload.put("op", opCode.getOpcode());
        payload.put("d", object);
    }

    public JSONObject getPayload() {
        return payload;
    }
}
