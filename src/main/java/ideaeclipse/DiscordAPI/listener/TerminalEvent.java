package ideaeclipse.DiscordAPI.listener;

import ideaeclipse.DiscordAPI.terminal.Terminal;
import ideaeclipse.reflectionListener.Event;

/**
 * Base event for all terminal Listeners
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
