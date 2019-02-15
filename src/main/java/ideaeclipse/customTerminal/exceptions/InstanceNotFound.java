package ideaeclipse.customTerminal.exceptions;

/**
 * When the instance isn't found
 *
 * @author Ideaeclipse
 */
public class InstanceNotFound extends Exception {
    public InstanceNotFound(){
        super("Command not found use !help");
    }
}
