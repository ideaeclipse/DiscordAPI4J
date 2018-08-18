package DiscordAPI.utils;

import DiscordAPI.objects.Builder;
import DiscordAPI.webSocket.Wss;
import DiscordAPI.webSocket.OpCodes;
import org.json.simple.JSONObject;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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
                Json object = Builder.buildPayload(OpCodes.Heartbeat, 251);
                webSocket.sendText(object);
                Thread.sleep(this.heartbeat);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
