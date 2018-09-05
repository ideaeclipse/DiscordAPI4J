package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

public class ExitingFunction extends TerminalEvent implements ListenerFeatures {

    public ExitingFunction(Terminal t) {
        super(t);
    }


    @Override
    public String getReturn() {
        return "Exiting Function";
    }

}
