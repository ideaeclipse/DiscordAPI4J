package DiscordAPI.objects;

import DiscordAPI.objects.Interfaces.IChannel;
import DiscordAPI.utils.DiscordLogger;
import DiscordAPI.utils.Json;
import DiscordAPI.webSocket.TextOpCodes;
import DiscordAPI.webSocket.VoiceWss;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;

import java.io.IOException;

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


    void joinChannel(final IChannel channel) {
        Builder.VoiceStateUpdate voiceStateUpdate = new Builder.VoiceStateUpdate();
        voiceStateUpdate.channel_id = channel.getId();
        voiceStateUpdate.guild_id = bot.getGuildId();
        Json json = Builder.buildPayload(TextOpCodes.Voice_State_Update, Builder.buildData(voiceStateUpdate));
        bot.getTextWss().sendText(json);
        synchronized (this.lock) {
            try {
                this.lock.wait();
                voiceWss = new VoiceWss(bot, vServerUpdate, initialUpdate, vServerUpdate.getEndpoint());
            } catch (InterruptedException | IOException | WebSocketException e) {
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
