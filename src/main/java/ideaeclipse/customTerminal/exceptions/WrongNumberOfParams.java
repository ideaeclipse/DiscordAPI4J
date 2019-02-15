package ideaeclipse.customTerminal.exceptions;

/**
 * When you passed the wrong number of params
 *
 * @author Ideaeclipse
 */
public class WrongNumberOfParams extends Exception {
    public WrongNumberOfParams(final int actual, final int expected){
        super("You passed " + actual + " params, the command expected " + expected);
    }
}
