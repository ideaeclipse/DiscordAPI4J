package ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.errors;

import ideaeclipse.DiscordAPI.Terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;

/**
 * This class is notified when a function is called that doesn't exist
 *
 * @author ideaeclipse
 */
public class InvalidCommand extends TerminalEvent {

    public InvalidCommand(Terminal t) {
        super(t);
    }

    public String getReturn() {
        return "That is an invalid command use command cm help";
    }

}
