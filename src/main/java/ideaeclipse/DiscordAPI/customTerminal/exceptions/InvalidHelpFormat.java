package ideaeclipse.DiscordAPI.customTerminal.exceptions;

public class InvalidHelpFormat extends Exception {
    public InvalidHelpFormat() {
        super("Invalid help format\nUse either cm help or cm help $package $command");
    }
}
