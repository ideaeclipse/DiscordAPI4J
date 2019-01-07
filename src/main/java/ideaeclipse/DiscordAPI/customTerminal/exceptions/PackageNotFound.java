package ideaeclipse.DiscordAPI.customTerminal.exceptions;

public class PackageNotFound extends Exception {
    public PackageNotFound(){
        super("Package not found use !help");
    }
}
