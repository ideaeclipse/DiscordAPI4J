package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

/**
 * Function is called when exiting a function keyword: done
 *
 * @author ideaeclipse
 */
public class ExitingFunction extends TerminalEvent implements ListenerFeatures {

    public ExitingFunction(final Terminal t) {
        super(t);
    }


    @Override
    public String getReturn() {
        return "Exiting Function";
    }

}
