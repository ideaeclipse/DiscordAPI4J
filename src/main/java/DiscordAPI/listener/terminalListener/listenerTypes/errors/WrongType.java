package DiscordAPI.listener.terminalListener.listenerTypes.errors;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

public class WrongType extends TerminalEvent implements ListenerFeatures {

    public WrongType(Terminal t) {
        super(t);
    }

    @Override
    public String getReturn() {
        return "Wrong Type";
    }

}
