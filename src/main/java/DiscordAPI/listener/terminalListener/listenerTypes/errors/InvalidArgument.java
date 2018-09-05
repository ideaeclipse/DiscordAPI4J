package DiscordAPI.listener.terminalListener.listenerTypes.errors;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

public class InvalidArgument extends TerminalEvent implements ListenerFeatures {

    public InvalidArgument(Terminal t) {
        super(t);
    }

    @Override
    public String getReturn() {
        return "Invalid Argument use the help command for more info";
    }

}
