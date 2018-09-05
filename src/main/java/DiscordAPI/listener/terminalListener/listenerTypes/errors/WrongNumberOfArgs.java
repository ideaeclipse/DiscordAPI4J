package DiscordAPI.listener.terminalListener.listenerTypes.errors;

import DiscordAPI.Terminal.NameConversion;
import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

public class WrongNumberOfArgs extends ListenerEvent implements ListenerFeatures {
    private Class<?>[] args;

    private WrongNumberOfArgs(Terminal t) {
        super(t);
    }

    public WrongNumberOfArgs(Terminal t, Class<?>[] c) {
        this(t);
        args = c;
    }

    public String getReturn() {
        return "Wrong Number of Args, you need to enter the following \n" + NameConversion.convert(args);
    }
}
