package ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import ideaeclipse.DiscordAPI.Terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;

/**
 * Function is called when exiting a function keyword: done
 *
 * @author ideaeclipse
 */
public class ExitingFunction extends TerminalEvent {

    public ExitingFunction(final Terminal t) {
        super(t);
    }


    public String getReturn() {
        return "Exiting Function";
    }

}
