package ideaeclipse.DiscordAPI.listener.discordApiListener;

import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.objects.Interfaces.IChannel;
import ideaeclipse.DiscordAPI.objects.ParserObjects;
import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

/**
 * This Class is called using java reflection
 * This Channel_Create.class value is stored in
 * ideaeclipse.DiscordAPI.webSocket.WebSocket_Events
 *
 * @author Ideaeclipse
 */
public class Channel_Create extends Event {
    private IChannel channel;

    /**
     * Initialized using Java reflection
     *
     * @param b       passed to super Used when user adds a listener event
     * @param payload the 'd' param from the message from the webscoekt
     * @see Wss under case Dispatch
     */
    public Channel_Create(final IPrivateBot b, final Json payload) {
        channel = new ParserObjects.ChannelCreate(b, payload).getChannel();
    }

    /**
     * @return Channel created using {@link ideaeclipse.DiscordAPI.objects.ParserObjects.ChannelCreate}
     */
    public IChannel getChannel() {
        return channel;
    }
}
