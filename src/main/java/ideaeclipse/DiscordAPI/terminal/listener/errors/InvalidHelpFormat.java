package ideaeclipse.DiscordAPI.terminal.listener.errors;

import ideaeclipse.DiscordAPI.terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.TerminalEvent;

import java.util.List;

/**
 * This function is for when a help command is of the improper format
 *
 * @author ideaeclipse
 */
public class InvalidHelpFormat extends TerminalEvent {
    private String word;
    private List<String> strings;

    private InvalidHelpFormat(final Terminal t) {
        super(t);
    }

    /**
     * @param t       current terminal session
     * @param word1   first word
     * @param subargs all possible subargs
     */
    public InvalidHelpFormat(final Terminal t, final String word1, final List<String> subargs) {
        this(t);
        word = word1;
        strings = subargs;
    }

    /**
     * @return return statement to be printed to user
     */
    public String getReturn() {
        StringBuilder string = new StringBuilder();
        string.append("Please enter a command looking like help ").append(word).append(" $Subargs").append(" $Subargs = ").append(strings);
        return String.valueOf(string);
    }

}
