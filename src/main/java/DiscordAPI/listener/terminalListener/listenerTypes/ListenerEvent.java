package DiscordAPI.listener.terminalListener.listenerTypes;

import DiscordAPI.Terminal.Terminal;

public abstract class ListenerEvent {
    private Terminal t;

    public ListenerEvent(Terminal t) {
        this.t = t;
    }

    public Terminal getTerminal() {
        return this.t;
    }
}
