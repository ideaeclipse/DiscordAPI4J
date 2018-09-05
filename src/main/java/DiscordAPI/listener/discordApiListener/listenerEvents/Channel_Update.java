package DiscordAPI.listener.discordApiListener.listenerEvents;

import DiscordAPI.IPrivateBot;
import DiscordAPI.listener.discordApiListener.listenerTypes.ListenerEvent;
import DiscordAPI.objects.Interfaces.IChannel;
import DiscordAPI.objects.Parser;
import DiscordAPI.utils.Json;
import DiscordAPI.webSocket.Wss;

/**
 * This Class is called using java reflection
 * This Channel_Create.class value is stored in
 * DiscordAPI.webSocket.WebSocket_Events
 *
 * @author Ideaeclipse
 */
public class Channel_Update extends ListenerEvent {
    private IChannel oldC;
    private IChannel newC;

    /**
     * Initialized using Java reflection
     *
     * @param b       passed to super Used when user adds a listener event
     * @param payload the 'd' param from the message from the webscoekt
     * @see Wss under case Dispatch
     */
    public Channel_Update(final IPrivateBot b, final Json payload) {
        super(b);
        Parser.ChannelUpdate parser = new Parser.ChannelUpdate(b, payload);
        oldC = parser.getOldChannel();
        newC = parser.getNewChannel();
    }

    /**
     * @return Original Channel created using {@link DiscordAPI.objects.Parser.ChannelCreate}
     */
    public IChannel getOldChannel() {
        return oldC;
    }

    /**
     * @return New Channel created using {@link DiscordAPI.objects.Parser.ChannelCreate}
     */
    public IChannel getNewChannel() {
        return newC;
    }
}
