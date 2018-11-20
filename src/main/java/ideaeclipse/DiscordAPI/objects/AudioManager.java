package ideaeclipse.DiscordAPI.objects;

import ideaeclipse.DiscordAPI.objects.Interfaces.IChannel;
import ideaeclipse.DiscordAPI.webSocket.TextOpCodes;
import ideaeclipse.DiscordAPI.webSocket.VoiceWss;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import ideaeclipse.JsonUtilities.Builder;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.customLogger.CustomLogger;
import ideaeclipse.customLogger.Level;

import java.io.IOException;
import java.util.List;

/**
 * Audio Manager is annotated as Deprecated because this part of the library has not been completed yet.
 * <p>
 * The purpose of the audiomanager is to start the voice socket connection with discord and manage all incoming dispatches
 * also allows you to connect to channels see {@link ideaeclipse.DiscordAPI.utils.DiscordUtils.Search#VOICECHANNEL(List, String)}
 *
 * @author ideaeclipse
 */
@Deprecated
public class AudioManager {
    private final CustomLogger logger;
    private final DiscordBot bot;
    private final Object lock;
    private VServerUpdate vServerUpdate;
    private VStateUpdate initialUpdate;
    private WebSocket webSocket;
    private VoiceWss voiceWss;

    /**
     * @param bot passes the bot
     */
    AudioManager(final DiscordBot bot) {
        this.logger = new CustomLogger(this.getClass(), bot.getLoggerManager());
        this.lock = new Object();
        this.bot = bot;
    }

    /**
     * Joins a channel {@link ideaeclipse.DiscordAPI.utils.DiscordUtils.Search#VOICECHANNEL(List, String)}+-
     *
     * @param channel
     */
    void joinChannel(final IChannel channel) {
        BuilderObjects.VoiceStateUpdate voiceStateUpdate = new BuilderObjects.VoiceStateUpdate();
        voiceStateUpdate.channel_id = channel.getId();
        voiceStateUpdate.guild_id = bot.getGuildId();
        Json json = Builder.buildPayload(TextOpCodes.Voice_State_Update.ordinal(), Builder.buildData(voiceStateUpdate));
        bot.getTextWss().sendText(json);
        /*
        only starts after both the update for the server and state are completed
         */
        synchronized (this.lock) {
            try {
                this.lock.wait();
                voiceWss = new VoiceWss(bot, vServerUpdate, initialUpdate, vServerUpdate.getEndpoint());
            } catch (InterruptedException | IOException | WebSocketException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param serverUpdate initial server update sent from the socket
     */
    void setVoiceServerUpdate(final VServerUpdate serverUpdate) {
        this.vServerUpdate = serverUpdate;
    }

    /**
     * @param initialUpdate initial state update sent from the socket
     */
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
