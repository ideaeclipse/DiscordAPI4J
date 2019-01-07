package ideaeclipse.DiscordAPI.customTerminal.exceptions;

public class SubCommandNotFound extends Exception {
    public SubCommandNotFound(){
        super("Command not found use !help");
    }
}
