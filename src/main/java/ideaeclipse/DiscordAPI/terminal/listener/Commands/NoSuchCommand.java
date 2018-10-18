package ideaeclipse.DiscordAPI.terminal.listener.Commands;

import ideaeclipse.DiscordAPI.terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.TerminalEvent;

/**
 * Function is called when there is a {@link NoSuchMethodException}
 * this is called when a user is inside a function and calls a command that doesn't exist
 *
 * @author ideaeclipse
 */
public class NoSuchCommand extends TerminalEvent {

    public NoSuchCommand(Terminal t) {
        super(t);
    }

    public String getReturn() {
        return "This method does not exist";
    }

}
