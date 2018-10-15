package ideaeclipse.DiscordAPI.listener.terminalListener.terminal;


import ideaeclipse.DiscordAPI.Terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.TerminalEvent;
import ideaeclipse.DiscordAPI.objects.Interfaces.IMessage;

/**
 * This listener is called when a function needs more input i.e a user entered a function or a user is still instead a function
 *
 * @author ideaeclipse
 */
public class NeedsMoreInput extends TerminalEvent {
    private IMessage m;


    public NeedsMoreInput(final Terminal t, final IMessage message) {
        super(t);
        m = message;
    }
    public String getReturn() {
        return null;
    }

    public IMessage getMessage() {
        return m;
    }
}
