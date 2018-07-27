package DiscordAPI.Objects.Audio;

import DiscordAPI.DiscordBot;
import DiscordAPI.WebSocket.JsonData.VoiceStateUpdate.VSUObject;
import com.neovisionaries.ws.client.WebSocket;
import org.json.simple.JSONObject;

public class AudioManager {
    private DiscordBot bot;
    private WebSocket socket;
    private Long guild_id;
    private VoiceAuth voiceAuth;

    public AudioManager(DiscordBot bot,WebSocket socket, Long guild_id){
        this.bot = bot;
        this.socket = socket;
        this.guild_id = guild_id;
        this.voiceAuth = new VoiceAuth(bot);
    }
    public AudioManager initialize(Long id){
        JSONObject object = new VSUObject(bot,guild_id).getVsu();
        object.put("channel_id",Long.parseUnsignedLong("471104815192211462"));
        JSONObject payload = new JSONObject();
        payload.put("op",4);
        payload.put("d",object);
        socket.sendText(String.valueOf(payload));
        return this;
    }

    public VoiceAuth getVoiceAuth() {
        return voiceAuth;
    }
}
