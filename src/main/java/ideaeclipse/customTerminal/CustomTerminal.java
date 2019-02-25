package ideaeclipse.customTerminal;

import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.customTerminal.exceptions.ImproperCommandFormat;
import ideaeclipse.customTerminal.exceptions.InvalidHelpFormat;
import ideaeclipse.customTerminal.exceptions.WrongNumberOfParams;
import ideaeclipse.customTerminal.exceptions.WrongParams;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @param <T> Method option parameter, if you want to pass a parameter to a "command" without having the user pass it you can declare it here.
 * @author Ideaeclipse
 */
public class CustomTerminal<T> {
    private final String prefix;
    private final InstanceHandler<T> terminal;
    private final Map<String, Command<T>> commands = new HashMap<>();

    /**
     * Generates all possible commands into {@link Command}
     * creates instance handler
     *
     * @param prefix           command prefix
     * @param methodParamClass method param class
     */
    public CustomTerminal(final IDiscordBot bot, final String prefix, final CommandsClass commandsClass, final Class<T> methodParamClass) {
        this.prefix = prefix;
        this.terminal = new InstanceHandler<>(this, methodParamClass);
        for (Method method : commandsClass.getClass().getDeclaredMethods()) {
            if (method.getAnnotation(Executable.class) != null) {
                this.commands.put(method.getName().toLowerCase(), new Command<>(bot, commandsClass, method.getName(), Arrays.asList(method.getParameterTypes()).contains(IDiscordBot.class), Arrays.asList(method.getParameterTypes()).contains(methodParamClass)));
            }
        }
    }

    /**
     * Handles input, creates instance if your command starts with the prefix, else searches for instance already existing else throws exception
     *
     * @param passed      command from user
     * @param methodParam method param possibly will be passed
     * @return object of some kind, normally a string
     * @throws ImproperCommandFormat if your command doesn't start with the prefix and you haven't created an instance
     */
    public Object handleInput(final String passed, final T methodParam) throws ImproperCommandFormat {
        if (passed.startsWith(prefix)) {
            ParseInput parseInput = new ParseInput(passed);
            List<String> input = parseInput.getWords(new LinkedList<>());
            if (input.get(0).equals(prefix.trim())) {
                input.remove(0);
            } else if (input.get(0).contains(prefix.trim())) {
                input.set(0, input.get(0).substring(prefix.length()));
            }
            if (this.commands.get(input.get(0).toLowerCase()) == null) {
                switch (input.size()) {
                    case 3:
                        if (input.get(0).equals("help"))
                            try {
                                return terminal.help(input, this.commands);
                            } catch (InvalidHelpFormat e) {
                                return e.getMessage();
                            }
                    case 1:
                        if (input.get(0).equals("help"))
                            try {
                                return terminal.help(input, this.commands);
                            } catch (InvalidHelpFormat e) {
                                return e.getMessage();
                            }
                        return "Did you mean " + this.prefix + "help?";
                    default:
                        throw new ImproperCommandFormat();
                }
            } else {
                Command<T> subCommand = this.commands.get(input.get(0).toLowerCase());
                input.remove(0);
                try {
                    return subCommand.invoke(methodParam, input.toArray());
                } catch (WrongNumberOfParams | WrongParams e) {
                    return e.getMessage();
                }

            }
        }
        throw new ImproperCommandFormat();
    }

    /**
     * @return command prefix
     */
    public String getPrefix() {
        return this.prefix;
    }
}
