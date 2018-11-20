package ideaeclipse.DiscordAPI.listener.discordApiListener;


import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.objects.Interfaces.IMessage;
import ideaeclipse.DiscordAPI.objects.ParserObjects;
import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

/**
 * This Class is called using java reflection
 * This Channel_Create.class value is stored in
 * ideaeclipse.DiscordAPI.webSocket.WebSocket_Events
 * If you need to know which type of channel the message came from compare it to ChannelTypes {@link ideaeclipse.DiscordAPI.objects.Payloads.ChannelTypes}
 *
 * @author Ideaeclipse
 */
public class Message_Create extends Event {
    private IMessage message;

    /**
     * Initialized using Java reflection
     *
     * @param b       passed to super Used when user adds a listener event
     * @param payload the 'd' param from the message from the websocket
     * @see Wss under case Dispatch
     */
    public Message_Create(final IPrivateBot b, final Json payload) {
        message = new ParserObjects.MessageCreate(b, payload).getMessage();
    }

    /**
     * @return returns Message created using {@link ideaeclipse.DiscordAPI.objects.ParserObjects.MessageCreate}
     */
    public IMessage getMessage() {
        return this.message;
    }
}
