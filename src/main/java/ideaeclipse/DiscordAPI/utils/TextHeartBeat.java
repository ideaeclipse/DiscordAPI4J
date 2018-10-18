package ideaeclipse.DiscordAPI.utils;

import ideaeclipse.DiscordAPI.webSocket.TextOpCodes;
import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.JsonUtilities.Builder;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.customLogger.CustomLogger;

public class TextHeartBeat implements Runnable {
    private final CustomLogger logger = new CustomLogger(Thread.currentThread(),this.getClass());
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
