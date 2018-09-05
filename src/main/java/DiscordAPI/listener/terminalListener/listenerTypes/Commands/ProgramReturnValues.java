package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

public class ProgramReturnValues extends ListenerEvent implements ListenerFeatures {
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
