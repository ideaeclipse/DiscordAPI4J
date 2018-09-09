package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

/**
 * This method is notified when a method has been called correcting and there is some sort of return values
 *
 * @author ideaeclipse
 */
public class ProgramReturnValues extends TerminalEvent implements ListenerFeatures {
    private String returnS;

    private ProgramReturnValues(final Terminal t) {
        super(t);
    }

    public ProgramReturnValues(final Terminal t, final String s) {
        this(t);
        returnS = s;
    }

    @Override
    public String getReturn() {
        return returnS;
    }
}
