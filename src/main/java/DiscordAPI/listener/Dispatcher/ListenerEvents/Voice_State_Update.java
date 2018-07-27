package DiscordAPI.listener.Dispatcher.ListenerEvents;

import DiscordAPI.DiscordBot;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;

public class Voice_State_Update extends ListenerEvent implements ListenerFeatures {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));

    public Voice_State_Update(DiscordAPI.DiscordBot b, JSONObject payload) {
        super(b);
        JSONObject d= (JSONObject) payload.get("d");
        b.getAudioManager().getVoiceAuth().setSession(String.valueOf(d.get("session_id")));
    }

    @Override
    public String getReturn() {
        return null;
    }
}
