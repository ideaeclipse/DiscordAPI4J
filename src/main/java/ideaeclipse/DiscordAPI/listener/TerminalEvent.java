package ideaeclipse.DiscordAPI.listener;

import ideaeclipse.DiscordAPI.Terminal.Terminal;
import ideaeclipse.reflectionListener.Event;

/**
 * Base event for all Terminal Listeners
 *
 * @author ideaeclipse
 */
public class TerminalEvent extends Event {
    private final Terminal t;

    public TerminalEvent(final Terminal t) {
        this.t = t;
    }

    public Terminal getTerminal() {
        return this.t;
    }
}
