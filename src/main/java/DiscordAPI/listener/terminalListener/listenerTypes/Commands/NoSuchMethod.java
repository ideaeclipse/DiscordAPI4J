package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

/**
 * Function is called when there is a {@link NoSuchMethodException}
 * this is called when a user is inside a function and calls a command that doesn't exist
 *
 * @author ideaeclipse
 */
public class NoSuchMethod extends TerminalEvent implements ListenerFeatures {

    public NoSuchMethod(Terminal t) {
        super(t);
    }

    @Override
    public String getReturn() {
        return "This method does not exist";
    }

}
