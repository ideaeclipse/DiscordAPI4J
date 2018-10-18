package ideaeclipse.DiscordAPI.terminal.listener.errors;

import ideaeclipse.DiscordAPI.terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.TerminalEvent;

/**
 * This function is notified when there is a wrong type of param inserted i.e String instead of int
 *
 * @author ideaeclipse
 */
public class WrongType extends TerminalEvent {

    public WrongType(final Terminal t) {
        super(t);
    }

    public String getReturn() {
        return "Wrong Type";
    }

}
