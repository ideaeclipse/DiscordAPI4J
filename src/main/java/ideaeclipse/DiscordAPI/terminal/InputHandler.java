package ideaeclipse.DiscordAPI.terminal;

import ideaeclipse.DiscordAPI.terminal.listener.Commands.BotCommands;
import ideaeclipse.DiscordAPI.terminal.listener.Commands.ClassInfo;
import ideaeclipse.DiscordAPI.terminal.listener.errors.InvalidCommand;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class handles all input from the eventmanager listener in {@link ideaeclipse.DiscordAPI.objects.TerminalManager}
 * This class is an instance for every current instance of the {@link Terminal} class
 *
 * @author ideaecipse
 */
class InputHandler {
    private final List<String> words;
    private final Terminal t;
    private final Class<?> a, d;
    private Function function;

    /**
     * @param words Words being passed
     * @param t     current terminal session
     */
    InputHandler(ArrayList<String> words, Terminal t) {
        this.words = words;
        this.t = t;
        this.a = t.getCommandList().getGenericCommandMap().get("adminCommands").get(0);
        this.d = t.getCommandList().getGenericCommandMap().get("defaultCommands").get(0);
    }

    /**
     * if true user has entered function
     *
     * @return returns whether a user has entered a function or not
     */
    boolean start() {
        switch (words.get(0)) {
            case "help":
                if (words.size() == 1) {
                    t.getDispatcher().callEvent(new BotCommands(t, d, a));
                } else if (words.size() == 3) {
                    Optional<Class<?>> optionalClass = getRequestedClass(t.getCommandList().getCommandMap(), words.get(1), words.get(2));
                    if (optionalClass.isPresent()) {
                        Class<?> requestedClass = optionalClass.get();
                        t.getDispatcher().callEvent(new ClassInfo(t, requestedClass.getDeclaredConstructors(), requestedClass.getDeclaredMethods(), d, a));
                    }
                }
                break;
            default:
                if (Function.generic.isGenericCommand(words.get(0), a, d)) {
                    Function.generic.invoke(t, words, a, d);
                } else {
                    if (words.size() == 2) {
                        Optional<Class<?>> optionalClass = getRequestedClass(t.getCommandList().getCommandMap(), words.get(0), words.get(1));
                        if (optionalClass.isPresent()) {
                            this.function = new Function(optionalClass.get(), t);
                            t.changeStatus(true);
                            return true;
                        }
                        t.getDispatcher().callEvent(new InvalidCommand(t));
                    } else if (words.size() >= 2) {
                        List<String> params = words;
                        Objects.requireNonNull(getRequestedClass(t.getCommandList().getCommandMap(), words.get(0), words.get(1))).ifPresent(aClass -> {
                            params.remove(0);
                            params.remove(0);
                            function = new Function(aClass, t, params);
                            t.changeStatus(true);
                        });
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    /**
     * @param map   {@link CommandList#getCommandMap()} map instance
     * @param key   the subcommand/subdirectory i.e proof/search
     * @param value the function name
     * @return an optional instance of a class
     */
    private Optional<Class<?>> getRequestedClass(Map<String, List<Class<?>>> map, String key, String value) {
        if (map.get(key) != null) {
            return Optional.of(map
                    .get(key)
                    .stream()
                    .filter(o -> o.getSimpleName().toLowerCase().trim().equals(value.toLowerCase().trim()))
                    .collect(Collectors.toList())
                    .get(0));
        }
        return Optional.empty();
    }

    Function getFunction() {
        return function;
    }
}

