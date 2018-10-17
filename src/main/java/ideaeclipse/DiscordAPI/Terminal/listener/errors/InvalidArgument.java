package ideaeclipse.DiscordAPI.terminal.listener.errors;

import ideaeclipse.DiscordAPI.terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.TerminalEvent;

/**
 * This function is notified when there is an invalid argument i.e a string instead of an integer
 *
 * @author ideaeclipse
 * @see WrongType
 */
public class InvalidArgument extends TerminalEvent{

    public InvalidArgument(final Terminal t) {
        super(t);
    }

    public String getReturn() {
        return "Invalid Argument use the help command for more info";
    }

}
