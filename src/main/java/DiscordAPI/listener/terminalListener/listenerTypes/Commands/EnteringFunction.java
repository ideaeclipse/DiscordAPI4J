package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

public class EnteringFunction extends TerminalEvent implements ListenerFeatures {
    private String function;

    private EnteringFunction(Terminal t) {
        super(t);
    }

    public EnteringFunction(Terminal t, String functionName) {
        this(t);
        function = functionName;
    }

    @Override
    public String getReturn() {
        return "Enter command for: " + this.function;
    }
}
