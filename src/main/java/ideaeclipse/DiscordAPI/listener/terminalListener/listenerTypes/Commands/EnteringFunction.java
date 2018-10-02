package ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import ideaeclipse.DiscordAPI.Terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;

public class EnteringFunction extends TerminalEvent {
    private String function;

    public EnteringFunction(final Terminal t, final String functionName) {
        super(t);
        function = functionName;
        t.setCurrentFunction(function);
    }

    public String getReturn() {
        return "Enter command for: " + this.function;
    }
}
