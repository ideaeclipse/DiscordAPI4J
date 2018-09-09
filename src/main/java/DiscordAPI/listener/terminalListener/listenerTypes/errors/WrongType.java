package DiscordAPI.listener.terminalListener.listenerTypes.errors;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

/**
 * This function is notified when there is a wrong type of param inserted i.e String instead of int
 *
 * @author ideaeclipse
 */
public class WrongType extends TerminalEvent implements ListenerFeatures {

    public WrongType(final Terminal t) {
        super(t);
    }

    @Override
    public String getReturn() {
        return "Wrong Type";
    }

}
