package DiscordAPI.listener.terminalListener.listenerTypes;

import DiscordAPI.Terminal.Terminal;

/**
 * Base event for all Terminal Listeners
 *
 * @author ideaeclipse
 */
public abstract class TerminalEvent {
    private final Terminal t;

    public TerminalEvent(final Terminal t) {
        this.t = t;
    }

    public Terminal getTerminal() {
        return this.t;
    }
}
