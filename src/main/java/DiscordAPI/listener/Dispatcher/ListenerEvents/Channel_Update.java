package DiscordAPI.listener.Dispatcher.ListenerEvents;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DChannel;
import DiscordAPI.WebSocket.Utils.ConvertJSON;
import DiscordAPI.WebSocket.Utils.DiscordLogger;
import DiscordAPI.WebSocket.Utils.Parsers.ChannelData;
import DiscordAPI.WebSocket.Utils.Search;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;

public class Channel_Update extends ListenerEvent implements ListenerFeatures {
    private DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private DChannel oldC;
    private DChannel newC;
    public Channel_Update(DiscordBot b, JSONObject payload) {
        super(b);
        JSONObject d = (JSONObject) ConvertJSON.convertToJSONOBJECT(String.valueOf(payload.get("d")));
        ChannelData cd = new ChannelData(d).logic();
        oldC = Search.CHANNEL(b.getChannels(),cd.getChannel().getName());
        newC = cd.getChannel();
        b.updateChannels();
        logger.info("Channel Update Old: Name: " + oldC.getName() + " NSFW: " + oldC.getNsfw() + " Position: " + oldC.getPosition());
        logger.info("Channel Update New: Name: " + newC.getName() + " NSFW: " + newC.getNsfw() + " Position: " + newC.getPosition());
    }

    public DChannel getOldChannel() {
        return oldC;
    }

    public DChannel getNewChannel() {
        return newC;
    }

    @Override
    public String getReturn() {
        return null;
    }
}
