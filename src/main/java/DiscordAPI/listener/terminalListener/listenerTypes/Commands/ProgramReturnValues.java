package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

public class ProgramReturnValues extends TerminalEvent implements ListenerFeatures {
    private String returnS;

    private ProgramReturnValues(Terminal t) {
        super(t);
    }

    public ProgramReturnValues(Terminal t, String s) {
        this(t);
        returnS = s;
    }

    @Override
    public String getReturn() {
        return returnS;
    }
}
