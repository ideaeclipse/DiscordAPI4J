package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

public class NoSuchMethod extends ListenerEvent implements ListenerFeatures {

    public NoSuchMethod(Terminal t) {
        super(t);
    }

    @Override
    public String getReturn() {
        return "This method does not exist";
    }

}
