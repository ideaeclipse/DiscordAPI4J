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
public class Channel_Update extends ListenerEvent {
    private Channel oldC;
    private Channel newC;

    /**
     * Initialized using Java reflection
     *
     * @param b       passed to super Used when user adds a listener event
     * @param payload the 'd' param from the message from the webscoekt
     * @see Wss under case Dispatch
     */
    public Channel_Update(final IDiscordBot b, final JSONObject payload) {
        super(b);
        Parser.ChannelUpdate parser = new Parser.ChannelUpdate(b, payload);
        oldC = parser.getOldChannel();
        newC = parser.getNewChannel();
    }

    /**
     * @return Original Channel created using {@link DiscordAPI.objects.Parser.ChannelCreate}
     */
    public Channel getOldChannel() {
        return oldC;
    }

    /**
     * @return New Channel created using {@link DiscordAPI.objects.Parser.ChannelCreate}
     */
    public Channel getNewChannel() {
        return newC;
    }
}
