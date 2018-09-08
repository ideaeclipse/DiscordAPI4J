package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

public class EnteringFunction extends TerminalEvent implements ListenerFeatures {
    private String function;

    private EnteringFunction(final Terminal t) {
        super(t);
    }

    public EnteringFunction(final Terminal t, final String functionName) {
        this(t);
        function = functionName;
        t.setCurrentFunction(function);
    }

    @Override
    public String getReturn() {
        return "Enter command for: " + this.function;
    }
}
