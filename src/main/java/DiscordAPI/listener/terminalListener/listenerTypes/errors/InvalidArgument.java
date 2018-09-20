package DiscordAPI.listener.terminalListener.listenerTypes.errors;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;

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
