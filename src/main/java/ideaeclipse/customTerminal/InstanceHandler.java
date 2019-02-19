package ideaeclipse.customTerminal;

import ideaeclipse.customTerminal.exceptions.InvalidHelpFormat;

import java.util.List;
import java.util.Map;

/**
 * Instance handler, will print generic help commands, instance help commands, and create new instances
 *
 * @param <T> Optional method param
 * @author Ideaeclipse
 */
class InstanceHandler<T> {
    private final CustomTerminal<T> terminal;
    private final Util<T> util;

    /**
     * @param methodParamClass method param class
     */
    InstanceHandler(final CustomTerminal<T> terminal, final Class<T> methodParamClass) {
        this.terminal = terminal;
        this.util = new Util<>(methodParamClass);
    }

    /**
     * Print help from input list
     *
     * @param input           input list
     * @param genericCommands genericCommands map
     * @return printable help menu
     * @throws InvalidHelpFormat     When the help format is invalid
     */
    String help(final List<String> input, final Map<String, Command<T>> genericCommands) throws InvalidHelpFormat {
        switch (input.size()) {
            case 1:
                StringBuilder builder = new StringBuilder();
                builder.append("<N>ConsoleCommands</N><V>");
                for (Map.Entry<String, Command<T>> entry : genericCommands.entrySet()) {
                    builder.append("`").append(this.terminal.getPrefix()).append(this.util.beautifyConstructor(entry.getValue().getMethod())).append("`").append("\n");
                }
                builder.append("</V>");
                builder.append("<N>Examples</N>");
                builder.append("<V>`Command Example:` !$genericCommand $params... with a space between each param.\nFor example add <int,int> could be executed as !add 5 5\n</V>");
                builder.append("<N>Info</N>");
                builder.append("<V>All valid commands are in the !help menu\nIf you want to send a string that contains spaces you need to surround it with single quotes for example `'this is one parameter'` vs `this is 4 parameters`</V>");
                return String.valueOf(builder);
            default:
                throw new InvalidHelpFormat();
        }
    }

}
