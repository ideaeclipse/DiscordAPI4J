package ideaeclipse.DiscordAPI.webSocket.rateLimit;

import java.io.IOException;

/**
 * Used for {@link HttpEvent} and {@link WebSocketEvent}
 *
 * @author Ideaeclipse
 */
public interface IQueueHandler {
    Object event() throws IOException;
}
