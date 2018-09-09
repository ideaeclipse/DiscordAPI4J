package DiscordAPI.listener.terminalListener.listenerTypes.errors;

import DiscordAPI.Terminal.NameConversion;
import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

/**
 * This function is notified when a user calls a method with the incorrect number of parameters
 *
 * @author ideaeclipse
 */
public class WrongNumberOfArgs extends TerminalEvent implements ListenerFeatures {
    private Class<?>[] args;

    private WrongNumberOfArgs(final Terminal t) {
        super(t);
    }

    /**
     * @param t current terminal session
     * @param c list of param types you should have / are required
     */
    public WrongNumberOfArgs(final Terminal t, final Class<?>[] c) {
        this(t);
        args = c;
    }

    public String getReturn() {
        return "Wrong Number of Args, you need to enter the following \n" + NameConversion.convert(args);
    }
}
