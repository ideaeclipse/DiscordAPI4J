package ideaeclipse.DiscordAPI.webSocket.rateLimit;

import com.neovisionaries.ws.client.WebSocket;
import ideaeclipse.JsonUtilities.Json;

/**
 * Used to execute websocket events
 *
 * @author Ideaeclipse
 * @see RateLimitRecorder
 */
public final class WebSocketEvent implements IQueueHandler {
    private final Json object;
    private final WebSocket socket;

    /**
     * @param socket websocket
     * @param object json object to send
     */
    public WebSocketEvent(final WebSocket socket, final Json object) {
        this.socket = socket;
        this.object = object;
    }

    @Override
    public Object event() {
        socket.sendText(object.toString());
        return null;
    }
}
