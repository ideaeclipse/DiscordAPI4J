package DiscordAPI.utils;

import DiscordAPI.webSocket.VoiceOpCodes;
import DiscordAPI.webSocket.VoiceWss;
import ideaeclipse.JsonUtilities.Builder;
import ideaeclipse.JsonUtilities.Json;

import java.io.Serializable;
import java.security.SecureRandom;

public class VoiceHeartBeat implements Runnable {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final VoiceWss webSocket;
    private volatile Integer heartbeat;
    private volatile boolean run;

    public VoiceHeartBeat(final VoiceWss ws, final Integer heartbeat) {
        logger.info("Initializing heartbeat function");
        this.webSocket = ws;
        this.heartbeat = heartbeat;
        this.run = true;
    }

    @Override
    public void run() {
        while (run) {
            try {
                Nonce nonce = new Nonce();
                Json object = Builder.buildPayload(VoiceOpCodes.HeartBeat.ordinal(), nonce.getRandom());
                webSocket.sendText(object);
                Thread.sleep(this.heartbeat);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private static class Nonce implements Serializable {

        private static final long serialVersionUID = 1L;
        private final static SecureRandom randomGenerator = new SecureRandom();

        private final int random;

        Nonce() {
            random = randomGenerator.nextInt();
        }

        int getRandom() {
            return random;
        }
    }
}
