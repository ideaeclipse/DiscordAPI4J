package ideaeclipse.customTerminal.exceptions;

/**
 * Called when the command format you entered was incorrect
 *
 * @author Ideaeclipse
 */
public class ImproperCommandFormat extends Exception {
    public ImproperCommandFormat(){
        super("Improper command use !help for commands");
    }
}
