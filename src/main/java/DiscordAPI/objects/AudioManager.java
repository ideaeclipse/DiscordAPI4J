package DiscordAPI.objects;

import DiscordAPI.utils.DiscordLogger;
import DiscordAPI.utils.Json;
import DiscordAPI.webSocket.OpCodes;
import DiscordAPI.webSocket.VoiceWss;
import com.neovisionaries.ws.client.WebSocket;

public class AudioManager {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final DiscordBot bot;
    private final Object lock;
    private VServerUpdate vServerUpdate;
    private VStateUpdate initialUpdate;
    private WebSocket webSocket;
    private VoiceWss voiceWss;

    AudioManager(final DiscordBot bot) {
        logger.setLevel(DiscordLogger.Level.TRACE);
        this.lock = new Object();
        this.bot = bot;
    }


    public void joinChannel(final VoiceChannel channel) {
        Builder.VoiceStateUpdate voiceStateUpdate = new Builder.VoiceStateUpdate();
        voiceStateUpdate.channel_id = channel.getId();
        voiceStateUpdate.guild_id = bot.getGuildId();
        Json json = Builder.buildPayload(OpCodes.Voice_State_Update, Builder.buildData(voiceStateUpdate));
        bot.getTextWss().sendText(json);
        synchronized (this.lock) {
            try {
                this.lock.wait();
                logger.debug(vServerUpdate.toString());
                logger.debug(initialUpdate.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void setVoiceServerUpdate(final VServerUpdate serverUpdate) {
        this.vServerUpdate = serverUpdate;
    }

    void setInitialUpdate(VStateUpdate initialUpdate) {
        this.initialUpdate = initialUpdate;
    }

    VServerUpdate getVoiceServerUpdate() {
        return this.vServerUpdate;
    }

    VStateUpdate getInitialUpdate() {
        return initialUpdate;
    }

    Object getLock() {
        return lock;
    }
}
