package ideaeclipse.DiscordAPI.utils;

import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.DiscordAPI.webSocket.TextOpCodes;
import ideaeclipse.JsonUtilities.Builder;
import ideaeclipse.JsonUtilities.Json;

public class TextHeartBeat implements Runnable {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final Wss webSocket;
    private volatile Long heartbeat;
    private volatile boolean run;

    public TextHeartBeat(final Wss ws, final Long heartbeat) {
        logger.info("Initializing heartbeat function");
        this.webSocket = ws;
        this.heartbeat = heartbeat;
        this.run = true;
    }

    @Override
    public void run() {
        while (run) {
            try {
                Json object = Builder.buildPayload(TextOpCodes.Heartbeat.ordinal(), 251);
                webSocket.sendText(object);
                Thread.sleep(this.heartbeat);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
