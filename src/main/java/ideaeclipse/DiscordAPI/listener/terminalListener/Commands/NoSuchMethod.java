package ideaeclipse.DiscordAPI.listener.terminalListener.Commands;

import ideaeclipse.DiscordAPI.Terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.TerminalEvent;

/**
 * Function is called when there is a {@link NoSuchMethodException}
 * this is called when a user is inside a function and calls a command that doesn't exist
 *
 * @author ideaeclipse
 */
public class NoSuchMethod extends TerminalEvent {

    public NoSuchMethod(Terminal t) {
        super(t);
    }

    public String getReturn() {
        return "This method does not exist";
    }

}
