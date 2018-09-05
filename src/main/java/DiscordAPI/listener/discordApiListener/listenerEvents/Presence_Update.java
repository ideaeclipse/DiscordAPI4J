package DiscordAPI.listener.discordApiListener.listenerEvents;

import DiscordAPI.IPrivateBot;
import DiscordAPI.listener.discordApiListener.ApiListener;
import DiscordAPI.objects.*;
import DiscordAPI.objects.Interfaces.IUser;
import DiscordAPI.utils.Json;
import DiscordAPI.webSocket.TextOpCodes;
import DiscordAPI.webSocket.Wss;

/**
 * This Class is called using java reflection
 * This Channel_Create.class value is stored in
 * DiscordAPI.webSocket.WebSocket_Events
 * This class uses the Presence Model {@link DiscordAPI.objects.Builder.Identity.Presence} {@link Builder#buildPayload(TextOpCodes, Object)} {@link Builder#buildData(Object)}
 * To set bots presence use {@link DiscordBot#setStatus(Payloads.GameTypes, String)}
 *
 * @author Ideaeclipse
 */
public class Presence_Update extends ApiListener {
    private IUser status;

    /**
     * Initialized using Java reflection
     *
     * @param b       passed to super Used when user adds a listener event
     * @param payload the 'd' param from the message from the webscoekt
     * @see Wss under case Dispatch
     */
    public Presence_Update(final IPrivateBot b, final Json payload) {
        super(b);
        status = new Parser.PresenceUpdate(b, payload).getUser();
    }

    /**
     * @return DiscordUser's Current Status {@link User}
     */
    public IUser getStatus() {
        return status;
    }
}
