package ideaeclipse.customTerminal;

import ideaeclipse.customTerminal.exceptions.ImproperCommandFormat;
import ideaeclipse.customTerminal.exceptions.InvalidHelpFormat;
import ideaeclipse.customTerminal.exceptions.WrongNumberOfParams;
import ideaeclipse.customTerminal.exceptions.WrongParams;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @param <T> Constructor optional parameter, if you want to pass a parameter to a instance constructor it will pass as this type, else it won't instantiate the class
 * @param <K> Method option parameter, if you want to pass a parameter to a "command" without having the user pass it you can declare it here.
 * @author Ideaeclipse
 */
public class CustomTerminal<T, K> {
    private final String prefix;
    private final InstanceHandler<T, K> terminal;
    private final Map<String, Command<?, K>> commands = new HashMap<>();

    /**
     * Generates all possible commands into {@link Command}
     * creates instance handler
     *
     * @param prefix           command prefix
     * @param commands         instance commands directory
     * @param constructorParam constructor param
     * @param methodParamClass method param class
     */
    public CustomTerminal(final String prefix, final String commands, final T constructorParam, final Class<K> methodParamClass) {
        this.prefix = prefix;
        this.terminal = new InstanceHandler<>(this, commands, constructorParam, methodParamClass);
        List<Class<?>> genericList = this.terminal.getList().getCommandMap().get("Commands");
        if (genericList == null) {
            System.err.println("You passed a directory: " + commands + " and said there was a commands class. This class cannot be found, ensure the class file is titled Commands. This is case sensitive");
            System.exit(-1);
        } else if (!genericList.isEmpty()) {
            try {
                Class<?> generic = genericList.get(0);
                List<Constructor<?>> constructors = Arrays.stream(generic.getDeclaredConstructors()).filter(o -> !Collections.disjoint(Arrays.asList(o.getParameterTypes()), Arrays.asList(constructorParam.getClass().getInterfaces()))).collect(Collectors.toList());
                Object o;
                if (constructors.isEmpty())
                    o = generic.getConstructor().newInstance();
                else
                    o = constructors.get(0).newInstance(constructorParam);
                for (Method method : generic.getDeclaredMethods()) {
                    if (method.getAnnotation(Executable.class) != null) {
                        this.commands.put(method.getName().toLowerCase(), new Command<>(o, method.getName(), Arrays.asList(method.getParameterTypes()).contains(methodParamClass)));
                    }
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
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
    public Object handleInput(final String passed, final K methodParam) throws ImproperCommandFormat {
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
                Command<?, K> subCommand = this.commands.get(input.get(0).toLowerCase());
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
