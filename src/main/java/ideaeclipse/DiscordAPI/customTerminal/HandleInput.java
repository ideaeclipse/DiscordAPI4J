package ideaeclipse.DiscordAPI.customTerminal;

import ideaeclipse.DiscordAPI.customTerminal.exceptions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class HandleInput<T, K, V> {
    private static final String prefix = "!";
    private final Map<T, Instance<?, V>> instanceList = new HashMap<>();
    private final CustomTerminal<K, V> terminal;
    private final Map<String, SubCommand<?, V>> subCommands = new HashMap<>();
    private final boolean useInstance = false;

    public HandleInput(final String commands, final String genericCommands, final K constructorParam, final Class<V> methodParamClass) {
        this.terminal = new CustomTerminal<>(commands, genericCommands, constructorParam, methodParamClass);
        List<Class<?>> genericList = this.terminal.getList().getGenericCommandMap().get("Generic");
        if (!genericList.isEmpty()) {
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
                        this.subCommands.put(method.getName().toLowerCase(), new SubCommand<>(o, method.getName(), Arrays.asList(method.getParameterTypes()).contains(methodParamClass)));
                    }
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public Object handleInput(final String passed, final T object, final V methodParam) throws ImproperCommandFormat {
        if (passed.startsWith(prefix)) {
            ParseInput parseInput = new ParseInput(passed);
            List<String> input = parseInput.getWords(new LinkedList<>());
            if (input.get(0).equals(prefix.trim())) {
                input.remove(0);
            } else if (input.get(0).contains(prefix.trim())) {
                input.set(0, input.get(0).substring(prefix.length()));
            }
            if (this.subCommands.get(input.get(0).toLowerCase()) == null) {
                switch (input.size()) {
                    case 3:
                        if (input.get(0).equals("help"))
                            try {
                                return terminal.help(input, this.subCommands,useInstance);
                            } catch (SubCommandNotFound | PackageNotFound | InvalidHelpFormat e) {
                                return e.getMessage();
                            }
                    case 2:
                        if (useInstance)
                            try {
                                instanceList.put(object, terminal.newInstance(input));
                                return "New Instance Added";
                            } catch (PackageNotFound | SubCommandNotFound | ConstructorNotFound e) {
                                return e.getMessage();
                            }
                        break;
                    case 1:
                        if (input.get(0).equals("help"))
                            try {
                                return terminal.help(input, this.subCommands,useInstance);
                            } catch (SubCommandNotFound | PackageNotFound | InvalidHelpFormat e) {
                                return e.getMessage();
                            }
                        return "Did you mean !help?";
                    default:
                        if (useInstance)
                            if (input.size() > 3) {
                                List<String> temp = new LinkedList<>();
                                temp.add(input.remove(0));
                                temp.add(input.remove(0));
                                try {
                                    instanceList.put(object, terminal.newInstance(temp, input.toArray()));
                                    return "New Instance Added";
                                } catch (PackageNotFound | SubCommandNotFound | ConstructorNotFound e) {
                                    return e.getMessage();
                                }
                            }
                        throw new ImproperCommandFormat();
                }
            } else {
                SubCommand<?, V> subCommand = this.subCommands.get(input.get(0).toLowerCase());
                input.remove(0);
                try {
                    return subCommand.invoke(methodParam, input.toArray());
                } catch (WrongNumberOfParams | WrongParams e) {
                    return e.getMessage();
                }

            }
        } else if (this.instanceList.containsKey(object) && useInstance) {
            ParseInput parseInput = new ParseInput(passed);
            List<String> input = parseInput.getWords(new LinkedList<>());
            Instance<?, V> currentInstance = this.instanceList.get(object);
            switch (input.get(0)) {
                case "done":
                    this.instanceList.remove(object);
                    return "Closing Instance";
                case "help":
                    return this.terminal.help(currentInstance);
                default:
                    try {
                        String command = input.get(0);
                        input.remove(0);
                        return currentInstance.execute(command, methodParam, input.toArray());
                    } catch (WrongNumberOfParams | WrongParams | SubCommandNotFound e) {
                        return e.getMessage();
                    }
            }
        }
        throw new ImproperCommandFormat();
    }
}
