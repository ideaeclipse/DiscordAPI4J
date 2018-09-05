package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

public class ExitingFunction extends ListenerEvent implements ListenerFeatures {

    public ExitingFunction(Terminal t) {
        super(t);
    }


    @Override
    public String getReturn() {
        return "Exiting Function";
    }

}
