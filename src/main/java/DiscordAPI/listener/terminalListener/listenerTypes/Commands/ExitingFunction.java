package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;

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
