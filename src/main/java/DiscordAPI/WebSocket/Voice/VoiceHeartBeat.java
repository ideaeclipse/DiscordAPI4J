package DiscordAPI.WebSocket.Voice;

import DiscordAPI.DiscordBot;
import DiscordAPI.WebSocket.JsonData.PAYLOAD;
import DiscordAPI.WebSocket.Utils.BuildJSON;
import DiscordAPI.WebSocket.Utils.ConvertJSON;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import com.neovisionaries.ws.client.WebSocket;
import org.json.simple.JSONObject;

import java.util.TimerTask;

public class VoiceHeartBeat extends TimerTask {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private WebSocket webSocket;
    private DiscordBot DiscordBot;
    private Long heartbeat;
    private final Long interval;

    public VoiceHeartBeat(WebSocket ws, DiscordBot DiscordBot,Long heartbeat) {
        logger.info("Initializing heartbeat function");
        this.webSocket = ws;
        this.DiscordBot = DiscordBot;
        this.heartbeat = heartbeat;
        this.interval = heartbeat;
    }

    @Override
    public void run() {
        JSONObject object = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(BuildJSON.BuildJSON(PAYLOAD.values(), this.DiscordBot)));
        object.put("op", 3);
        object.put("d", heartbeat);
        webSocket.sendText(String.valueOf(object));
        heartbeat = heartbeat+interval;
    }
}
