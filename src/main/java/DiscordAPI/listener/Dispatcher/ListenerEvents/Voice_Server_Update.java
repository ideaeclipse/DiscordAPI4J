package DiscordAPI.listener.Dispatcher.ListenerEvents;

import DiscordAPI.DiscordBot;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;

public class Voice_Server_Update extends ListenerEvent implements ListenerFeatures {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));

    public Voice_Server_Update(DiscordBot b, JSONObject payload) {
        super(b);
        JSONObject d= (JSONObject) payload.get("d");
        b.getAudioManager().getVoiceAuth().setEndpoint(String.valueOf(d.get("endpoint")));
        b.getAudioManager().getVoiceAuth().setGuild_id(Long.parseLong(String.valueOf(d.get("guild_id"))));
        b.getAudioManager().getVoiceAuth().setToken(String.valueOf(d.get("token")));
        if (b.getAudioManager().getVoiceAuth().getSession() != null) {
            System.out.println(b.getAudioManager().getVoiceAuth().getEndpoint());
            b.getAudioManager().getVoiceAuth().authenticate(b.getAudioManager());
        } else {
            logger.error("JSON data was parsed incorrectly");
        }
    }

    @Override
    public String getReturn() {
        return null;
    }
}
