package ideaeclipse.customTerminal.exceptions;


import java.util.List;

/**
 * Thrown when you passed the right number of params but they're wrong type, i.e string instead of int
 *
 * @author Ideaeclipse
 */
public class WrongParams extends Exception {
    public WrongParams(final List<Class<?>> passed, final List<Class<?>> expected) {
        super("You passed params with types " + passed + " the method expected " + expected);
    }
}
