package ideaeclipse.DiscordAPI.customTerminal.exceptions;

import java.util.List;

public class ConstructorNotFound extends Exception {
    public ConstructorNotFound(final String command, List<List<Class<?>>> options){
        super("Invalid amount of arguments to initialize command: " + command + "\n Your options are " + options);
    }
}
