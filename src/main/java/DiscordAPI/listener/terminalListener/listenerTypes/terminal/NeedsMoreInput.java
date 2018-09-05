package DiscordAPI.listener.terminalListener.listenerTypes.terminal;


import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;
import DiscordAPI.objects.Interfaces.IMessage;

public class NeedsMoreInput extends TerminalEvent implements ListenerFeatures {
    private IMessage m;

    private NeedsMoreInput(Terminal t) {
        super(t);
    }

    public NeedsMoreInput(Terminal t, IMessage message) {
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
