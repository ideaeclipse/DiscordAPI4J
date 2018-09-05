package DiscordAPI.listener.terminalListener.listenerTypes.errors;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

import java.util.List;

public class MissingParameters extends ListenerEvent implements ListenerFeatures {
    private List args;

    private MissingParameters(Terminal t) {
        super(t);
    }

    public MissingParameters(Terminal t, List l) {
        this(t);
        args = l;
    }

    @Override
    public String getReturn() {
        StringBuilder builder = new StringBuilder();
        builder.append("This command needs parameters your options are: ").append("\n").append(args);
        return String.valueOf(builder);
    }
}
