package DiscordAPI.webSocket.utils;

import DiscordAPI.DiscordBot;
import DiscordAPI.webSocket.jsonData.PAYLOAD;
import com.neovisionaries.ws.client.WebSocket;
import org.json.simple.JSONObject;

import java.util.TimerTask;

public class HeartBeat extends TimerTask {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private WebSocket webSocket;
    private DiscordBot DiscordBot;

    public HeartBeat(WebSocket ws, DiscordBot DiscordBot) {
        logger.info("Initializing heartbeat function");
        this.webSocket = ws;
        this.DiscordBot = DiscordBot;
    }

    @Override
    public void run() {
        JSONObject object = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(DiscordUtils.BuildJSON.BuildJSON(PAYLOAD.values())));
        object.put("op", 1);
        object.put("d", 251);
        webSocket.sendText(String.valueOf(object));
    }
}
