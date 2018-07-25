package DiscordAPI.WebSocket.Utils;

import DiscordAPI.Bot.BotImpl;
import DiscordAPI.WebSocket.JsonData.PAYLOAD;
import com.neovisionaries.ws.client.WebSocket;
import org.json.simple.JSONObject;

import java.util.TimerTask;

public class HeartBeat extends TimerTask {
    private WebSocket webSocket;
    private BotImpl botImpl;

    public HeartBeat(WebSocket ws, BotImpl botImpl) {
        this.webSocket = ws;
        this.botImpl = botImpl;
    }

    @Override
    public void run() {
        JSONObject object = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(BuildJSON.BuildJSON(PAYLOAD.values(), this.botImpl)));
        object.put("op", 1);
        object.put("d", 251);
        webSocket.sendText(String.valueOf(object));
    }
}
