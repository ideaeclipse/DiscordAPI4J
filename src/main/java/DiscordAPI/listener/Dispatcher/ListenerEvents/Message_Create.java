package DiscordAPI.listener.Dispatcher.ListenerEvents;

import DiscordAPI.Bot.BotImpl;
import DiscordAPI.Objects.DMessage;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;

public class Message_Create extends ListenerEvent implements ListenerFeatures {
    private DMessage message;

    private Message_Create(BotImpl b) {
        super(b);
    }

    public Message_Create(BotImpl b, DMessage message) {
        this(b);
        this.message = message;
    }

    public DMessage getMessage() {
        return this.message;
    }

    @Override
    public String getReturn() {
        return "";
    }
}
