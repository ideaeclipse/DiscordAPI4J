package DiscordAPI.listener.dispatcher.listenerEvents;


import DiscordAPI.IDiscordBot;
import DiscordAPI.objects.*;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.listenerTypes.ListenerFeatures;
import org.json.simple.JSONObject;
/*
When adding the listener you need to getChannel().getType() and compare it to ChannelTypes
 */
public class Message_Create extends ListenerEvent implements ListenerFeatures {
    private Message message;

    public Message_Create(final IDiscordBot b, final JSONObject object) {
        super(b);
        message = new Parser.MC(b,object).getMessage();
    }


    public Message getMessage() {
        return this.message;
    }

    @Override
    public String getReturn() {
        return "";
    }
}
