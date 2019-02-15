package ideaeclipse.customTerminal.exceptions;

/**
 * Called when a Invalid help command was performed
 *
 * @author Ideaeclipse
 */
public class InvalidHelpFormat extends Exception {
    public InvalidHelpFormat() {
        super("Invalid help format\nUse either cm help or cm help $package $command");
    }
}
