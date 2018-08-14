package DiscordAPI.listener.dispatcher.listenerEvents;

import DiscordAPI.IDiscordBot;
import DiscordAPI.objects.*;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;

public class Presence_Update extends ListenerEvent implements ListenerFeatures {
    private Status status;

    public Presence_Update(final IDiscordBot t, final JSONObject payload) {
        super(t);
        status = new Parser.PresenceUpdate(t,payload).getStatus();
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String getReturn() {
        return "yes";
    }
}
