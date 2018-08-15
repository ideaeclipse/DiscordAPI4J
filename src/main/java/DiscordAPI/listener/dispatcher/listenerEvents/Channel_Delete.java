package DiscordAPI.listener.dispatcher.listenerEvents;

import DiscordAPI.IDiscordBot;
import DiscordAPI.objects.Channel;
import DiscordAPI.objects.Parser;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.webSocket.Wss;
import org.json.simple.JSONObject;

/**
 * This Class is called using java reflection
 * This Channel_Create.class value is stored in
 * DiscordAPI.webSocket.WebSocket_Events
 *
 * @author Ideaeclipse
 */
public class Channel_Delete extends ListenerEvent{
    private Channel channel;


    /**
     * Initialized using Java reflection
     *
     * @param b       passed to super Used when user adds a listener event
     * @param payload the 'd' param from the message from the webscoekt
     * @see Wss under case Dispatch
     */
    public Channel_Delete(final IDiscordBot b, final JSONObject payload) {
        super(b);
        channel = new Parser.ChannelDelete(b, payload).getChannel();
    }
    /**
     * @return Channel created using {@link DiscordAPI.objects.Parser.ChannelCreate}
     */
    public Channel getChannel() {
        return channel;
    }
}
