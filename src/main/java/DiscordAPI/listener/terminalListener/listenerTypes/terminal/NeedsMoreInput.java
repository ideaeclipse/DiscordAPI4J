package DiscordAPI.listener.terminalListener.listenerTypes.terminal;


import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;
import DiscordAPI.objects.Interfaces.IMessage;

/**
 * This listener is called when a function needs more input i.e a user entered a function or a user is still instead a function
 *
 * @author ideaeclipse
 */
public class NeedsMoreInput extends TerminalEvent implements ListenerFeatures {
    private IMessage m;

    private NeedsMoreInput(final Terminal t) {
        super(t);
    }

    public NeedsMoreInput(final Terminal t, final IMessage message) {
        this(t);
        m = message;
    }

    @Override
    public String getReturn() {
        return null;
    }

    public IMessage getMessage() {
        return m;
    }
}
