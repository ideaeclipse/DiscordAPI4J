package DiscordAPI.listener.Dispatcher.ListenerEvents;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DChannel;
import DiscordAPI.WebSocket.Utils.ConvertJSON;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import DiscordAPI.WebSocket.Utils.Parsers.ChannelData;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;

public class Channel_Delete extends ListenerEvent implements ListenerFeatures {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private DChannel channel;

    public Channel_Delete(DiscordBot b, JSONObject payload) {
        super(b);
        JSONObject d = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(payload.get("d")));
        ChannelData cd = new ChannelData(d).logic();
        channel = cd.getChannel();
        b.updateChannels();
        logger.info("Channel Delete: Channel Name: " + channel.getName() + " NSFW: " + channel.getNsfw() + " Position: " + channel.getPosition());
    }

    public DChannel getChannel() {
        return channel;
    }

    @Override
    public String getReturn() {
        return null;
    }
}
