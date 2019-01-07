package ideaeclipse.DiscordAPI.customTerminal.exceptions;

public class WrongNumberOfParams extends Exception {
    public WrongNumberOfParams(final int actual, final int expected){
        super("You passed " + actual + " params, the command expected " + expected);
    }
}
