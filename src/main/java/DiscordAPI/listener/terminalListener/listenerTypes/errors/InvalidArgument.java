package DiscordAPI.listener.terminalListener.listenerTypes.errors;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

public class InvalidArgument extends ListenerEvent implements ListenerFeatures {

    public InvalidArgument(Terminal t) {
        super(t);
    }

    @Override
    public String getReturn() {
        return "Invalid Argument use the help command for more info";
    }

}
