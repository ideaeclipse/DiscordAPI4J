package ideaeclipse.DiscordAPI.listener.terminalListener.Commands;

import ideaeclipse.DiscordAPI.Terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.TerminalEvent;

/**
 * This method is notified when a method has been called correcting and there is some sort of return values
 *
 * @author ideaeclipse
 */
public class ProgramReturnValues extends TerminalEvent {
    private String returnS;

    public ProgramReturnValues(final Terminal t, final String s) {
        super(t);
        returnS = s;
    }

    public String getReturn() {
        return returnS;
    }
}
