package DiscordAPI.listener.dispatcher.listenerEvents;

import DiscordAPI.IDiscordBot;
import DiscordAPI.objects.Channel;
import DiscordAPI.objects.Parser;
import DiscordAPI.utils.DiscordLogger;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;

public class Channel_Delete extends ListenerEvent implements ListenerFeatures {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private Channel channel;

    public Channel_Delete(final IDiscordBot b, final JSONObject payload) {
        super(b);
        channel = new Parser.ChannelDelete(b, payload).getChannel();
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public String getReturn() {
        return null;
    }
}
