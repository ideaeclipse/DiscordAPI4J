package DiscordAPI.listener.terminalListener.listenerTypes.errors;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;

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
