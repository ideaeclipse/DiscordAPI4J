package DiscordAPI.listener.dispatcher.listenerEvents;


import DiscordAPI.IDiscordBot;
import DiscordAPI.objects.*;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.utils.Json;
import DiscordAPI.webSocket.Wss;
import org.json.simple.JSONObject;

/**
 * This Class is called using java reflection
 * This Channel_Create.class value is stored in
 * DiscordAPI.webSocket.WebSocket_Events
 * If you need to know which type of channel the message came from compare it to ChannelTypes {@link DiscordAPI.objects.Payloads.ChannelTypes}
 *
 * @author Ideaeclipse
 */
public class Message_Create extends ListenerEvent {
    private Message message;

    /**
     * Initialized using Java reflection
     *
     * @param b       passed to super Used when user adds a listener event
     * @param payload the 'd' param from the message from the webscoekt
     * @see Wss under case Dispatch
     */
    public Message_Create(final IDiscordBot b, final Json payload) {
        super(b);
        message = new Parser.MessageCreate(b, payload).getMessage();
    }

    /**
     * @return returns Message created using {@link DiscordAPI.objects.Parser.MessageCreate}
     */
    public Message getMessage() {
        return this.message;
    }
}
