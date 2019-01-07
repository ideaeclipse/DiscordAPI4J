package ideaeclipse.DiscordAPI.customTerminal.exceptions;


import java.util.List;

public class WrongParams extends Exception {
    public WrongParams(final List<Class<?>> passed, final List<Class<?>> expected) {
        super("You passed params with types " + passed + " the method expected " + expected);
    }
}
