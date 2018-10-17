package ideaeclipse.DiscordAPI.terminal.listener.Commands;

import ideaeclipse.DiscordAPI.terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.TerminalEvent;

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
