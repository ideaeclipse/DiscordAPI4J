package DiscordAPI.listener.dispatcher.listenerEvents;

import DiscordAPI.DiscordBot;
import DiscordAPI.objects.DChannel;
import DiscordAPI.webSocket.utils.DiscordLogger;
import DiscordAPI.webSocket.utils.parsers.ChannelP;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;

public class Channel_Create extends ListenerEvent implements ListenerFeatures {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private DChannel channel;

    public Channel_Create(DiscordBot b, JSONObject payload) {
        super(b);
        ChannelP cd = new ChannelP(payload).logic();
        channel = cd.getChannel();
        b.updateChannels();
        logger.info("Channel Create: Channel Name: " + channel.getName() + " NSFW: " + channel.getNsfw() + " Position: " + channel.getPosition());
    }

    public DChannel getChannel() {
        return channel;
    }

    @Override
    public String getReturn() {
        return null;
    }
}
