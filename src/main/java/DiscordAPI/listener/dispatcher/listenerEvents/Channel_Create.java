package DiscordAPI.listener.dispatcher.listenerEvents;

import DiscordAPI.IDiscordBot;
import DiscordAPI.objects.Channel;
import DiscordAPI.objects.Parser;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;

public class Channel_Create extends ListenerEvent implements ListenerFeatures {
    private Channel channel;

    public Channel_Create(final IDiscordBot b, final JSONObject payload) {
        super(b);
        channel = new Parser.CC(b, payload).getChannel();
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public String getReturn() {
        return null;
    }
}
