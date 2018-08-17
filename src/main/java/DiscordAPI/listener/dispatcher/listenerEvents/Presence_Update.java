package DiscordAPI.listener.dispatcher.listenerEvents;

import DiscordAPI.IDiscordBot;
import DiscordAPI.objects.*;
import DiscordAPI.listener.listenerTypes.ListenerEvent;
import DiscordAPI.utils.Json;
import DiscordAPI.webSocket.OpCodes;
import DiscordAPI.webSocket.Wss;

/**
 * This Class is called using java reflection
 * This Channel_Create.class value is stored in
 * DiscordAPI.webSocket.WebSocket_Events
 * This class uses the Presence Model {@link DiscordAPI.objects.Builder.Identity.Presence} {@link Builder#buildPayload(OpCodes, Object)} {@link Builder#buildData(Object)}
 * To set bots presence use {@link DiscordBot#setStatus(Payloads.GameTypes, String)}
 *
 * @author Ideaeclipse
 */
public class Presence_Update extends ListenerEvent {
    private Status status;

    /**
     * Initialized using Java reflection
     *
     * @param b       passed to super Used when user adds a listener event
     * @param payload the 'd' param from the message from the webscoekt
     * @see Wss under case Dispatch
     */
    public Presence_Update(final IDiscordBot b, final Json payload) {
        super(b);
        status = new Parser.PresenceUpdate(b, payload).getStatus();
    }

    /**
     * @return User's Current Status {@link Status}
     */
    public Status getStatus() {
        return status;
    }
}
