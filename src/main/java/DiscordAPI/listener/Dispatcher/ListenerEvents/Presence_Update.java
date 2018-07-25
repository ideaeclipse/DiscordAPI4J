package DiscordAPI.listener.Dispatcher.ListenerEvents;

import DiscordAPI.Bot.BotImpl;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;

public class Presence_Update extends ListenerEvent implements ListenerFeatures {

    public Presence_Update(BotImpl t) {
        super(t);
    }
    @Override
    public String getReturn() {
        return "yes";
    }
}
