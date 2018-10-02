package ideaeclipse.DiscordAPI.listener.discordApiListener.listenerEvents;

import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.objects.*;
import ideaeclipse.DiscordAPI.objects.Interfaces.IUser;
import ideaeclipse.DiscordAPI.webSocket.TextOpCodes;
import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

/**
 * This Class is called using java reflection
 * This Channel_Create.class value is stored in
 * ideaeclipse.DiscordAPI.webSocket.WebSocket_Events
 * This class uses the Presence Model {@link ideaeclipse.DiscordAPI.objects.Builder.Identity.Presence} {@link Builder#buildPayload(TextOpCodes, Object)} {@link Builder#buildData(Object)}
 * To set bots presence use {@link DiscordBot#setStatus(Payloads.GameTypes, String)}
 *
 * @author Ideaeclipse
 */
public class Presence_Update extends Event {
    private IUser status;

    /**
     * Initialized using Java reflection
     *
     * @param b       passed to super Used when user adds a listener event
     * @param payload the 'd' param from the message from the webscoekt
     * @see Wss under case Dispatch
     */
    public Presence_Update(final IPrivateBot b, final Json payload) {
        status = new ParserObjects.PresenceUpdate(b, payload).getUser();
    }

    /**
     * @return DiscordUser's Current Status {@link User}
     */
    public IUser getStatus() {
        return status;
    }
}
