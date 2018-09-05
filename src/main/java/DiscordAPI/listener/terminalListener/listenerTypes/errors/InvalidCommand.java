package DiscordAPI.listener.terminalListener.listenerTypes.errors;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

public class InvalidCommand extends ListenerEvent implements ListenerFeatures {

    public InvalidCommand(Terminal t) {
        super(t);
    }

    @Override
    public String getReturn() {
        return "That is an invalid command use command cm help";
    }

}
