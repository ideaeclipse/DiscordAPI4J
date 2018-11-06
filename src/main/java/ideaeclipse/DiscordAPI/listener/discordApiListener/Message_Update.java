package ideaeclipse.DiscordAPI.listener.discordApiListener;

import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.JsonUtilities.Json;

public class Message_Update extends Message_Create {
    /**
     * Initialized using Java reflection
     *
     * @param b       passed to super Used when user adds a listener event
     * @param payload the 'd' param from the message from the webscoekt
     * @see Wss under case Dispatch
     */
    public Message_Update(IPrivateBot b, Json payload) {
        super(b, payload);
    }
}
