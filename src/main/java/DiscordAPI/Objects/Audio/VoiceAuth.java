package DiscordAPI.Objects.Audio;

import DiscordAPI.DiscordBot;
import DiscordAPI.WebSocket.VoiceWss;
import com.neovisionaries.ws.client.WebSocketException;
import org.json.simple.JSONObject;

import java.io.IOException;

public class VoiceAuth {
    private String endpoint;
    private String token;
    private String session;
    private Long guild_id;
    private DiscordBot bot;
    public VoiceAuth(DiscordBot bot){
        this.bot = bot;
    }
    public Long getGuild_id() {
        return guild_id;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getSession() {
        return session;
    }

    public String getToken() {
        return token;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setGuild_id(Long guild_id) {
        this.guild_id = guild_id;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public void authenticate(AudioManager audioManager){
        try {
            VoiceWss.connect(bot,audioManager);
        } catch (IOException | WebSocketException e) {
            e.printStackTrace();
        }
    }
}
