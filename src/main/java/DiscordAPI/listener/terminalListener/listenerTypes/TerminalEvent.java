package DiscordAPI.listener.terminalListener.listenerTypes;

import DiscordAPI.Terminal.Terminal;

public abstract class TerminalEvent {
    private Terminal t;

    public TerminalEvent(Terminal t) {
        this.t = t;
    }

    public Terminal getTerminal() {
        return this.t;
    }
}
