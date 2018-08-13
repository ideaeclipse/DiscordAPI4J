package DiscordAPI.utils;

import DiscordAPI.webSocket.Wss;
import DiscordAPI.webSocket.jsonData.PAYLOAD;
import com.neovisionaries.ws.client.WebSocket;
import org.json.simple.JSONObject;

public class HeartBeat implements Runnable {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final Wss webSocket;
    private volatile Long heartbeat;
    private volatile boolean run;

    public HeartBeat(final Wss ws, final Long heartbeat) {
        logger.info("Initializing heartbeat function");
        this.webSocket = ws;
        this.heartbeat = heartbeat;
        this.run = true;
    }

    @Override
    public void run() {
        while (run) {
            try {
                JSONObject object = (JSONObject) DiscordUtils.convertToJSONOBJECT(String.valueOf(DiscordUtils.BuildJSON.BuildJSON(PAYLOAD.values())));
                object.put("op", 1);
                object.put("d", 251);
                webSocket.sendText(String.valueOf(object));
                Thread.sleep(this.heartbeat);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
