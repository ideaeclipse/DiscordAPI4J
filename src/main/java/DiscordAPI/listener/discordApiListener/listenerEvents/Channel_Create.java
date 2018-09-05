package DiscordAPI.listener.discordApiListener.listenerEvents;

import DiscordAPI.IPrivateBot;
import DiscordAPI.objects.Interfaces.IChannel;
import DiscordAPI.objects.Parser;
import DiscordAPI.listener.discordApiListener.ApiListener;
import DiscordAPI.utils.Json;
import DiscordAPI.webSocket.Wss;

/**
 * This Class is called using java reflection
 * This Channel_Create.class value is stored in
 * DiscordAPI.webSocket.WebSocket_Events
 *
 * @author Ideaeclipse
 */
public class Channel_Create extends ApiListener {
    private IChannel channel;

    /**
     * Initialized using Java reflection
     *
     * @param b       passed to super Used when user adds a listener event
     * @param payload the 'd' param from the message from the webscoekt
     * @see Wss under case Dispatch
     */
    public Channel_Create(final IPrivateBot b, final Json payload) {
        super(b);
        channel = new Parser.ChannelCreate(b, payload).getChannel();
    }

    /**
     * @return Channel created using {@link DiscordAPI.objects.Parser.ChannelCreate}
     */
    public IChannel getChannel() {
        return channel;
    }
}
