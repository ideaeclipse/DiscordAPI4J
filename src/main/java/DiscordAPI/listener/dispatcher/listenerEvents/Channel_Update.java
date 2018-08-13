package DiscordAPI.listener.dispatcher.listenerEvents;

import DiscordAPI.IDiscordBot;
import DiscordAPI.objects.Channel;
import DiscordAPI.objects.Parser;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;

public class Channel_Update extends ListenerEvent implements ListenerFeatures {
    private Channel oldC;
    private Channel newC;

    public Channel_Update(final IDiscordBot b, final JSONObject payload) {
        super(b);
        Parser.CU parser = new Parser.CU(b, payload);
        oldC = parser.getOldChannel();
        newC = parser.getNewChannel();
    }

    public Channel getOldChannel() {
        return oldC;
    }

    public Channel getNewChannel() {
        return newC;
    }

    @Override
    public String getReturn() {
        return null;
    }
}
