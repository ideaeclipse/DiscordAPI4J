package DiscordAPI.listener.discordApiListener.listenerEvents;


import DiscordAPI.IPrivateBot;
import DiscordAPI.objects.*;
import DiscordAPI.listener.discordApiListener.listenerTypes.ListenerEvent;
import DiscordAPI.objects.Interfaces.IMessage;
import DiscordAPI.utils.Json;
import DiscordAPI.webSocket.Wss;

/**
 * This Class is called using java reflection
 * This Channel_Create.class value is stored in
 * DiscordAPI.webSocket.WebSocket_Events
 * If you need to know which type of channel the message came from compare it to ChannelTypes {@link DiscordAPI.objects.Payloads.ChannelTypes}
 *
 * @author Ideaeclipse
 */
public class Message_Create extends ListenerEvent {
    private IMessage message;

    /**
     * Initialized using Java reflection
     *
     * @param b       passed to super Used when user adds a listener event
     * @param payload the 'd' param from the message from the webscoekt
     * @see Wss under case Dispatch
     */
    public Message_Create(final IPrivateBot b, final Json payload) {
        super(b);
        message = new Parser.MessageCreate(b, payload).getMessage();
    }

    /**
     * @return returns Message created using {@link DiscordAPI.objects.Parser.MessageCreate}
     */
    public IMessage getMessage() {
        return this.message;
    }
}
