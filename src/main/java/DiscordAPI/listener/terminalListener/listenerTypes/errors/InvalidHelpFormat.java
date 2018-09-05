package DiscordAPI.listener.terminalListener.listenerTypes.errors;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

import java.util.Arrays;

public class InvalidHelpFormat extends TerminalEvent implements ListenerFeatures {
    private String word;
    private String[] strings;

    private InvalidHelpFormat(Terminal t) {
        super(t);
    }

    public InvalidHelpFormat(Terminal t, String word1, String[] subargs) {
        this(t);
        word = word1;
        strings = subargs;
    }
    @Override
    public String getReturn() {
        StringBuilder string = new StringBuilder();
        string.append("Please enter a command looking like help ").append(word).append(" $Subargs").append(" $Subargs = ").append(Arrays.toString(strings));
        return String.valueOf(string);
    }

}
