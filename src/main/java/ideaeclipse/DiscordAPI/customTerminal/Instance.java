package ideaeclipse.DiscordAPI.customTerminal;

import ideaeclipse.DiscordAPI.customTerminal.exceptions.SubCommandNotFound;
import ideaeclipse.DiscordAPI.customTerminal.exceptions.WrongNumberOfParams;
import ideaeclipse.DiscordAPI.customTerminal.exceptions.WrongParams;

import java.lang.reflect.Method;
import java.util.*;

public class Instance<T, K> {
    private final T object;
    private final Class<?> instanceClass;
    private final Map<String, SubCommand<T, K>> subCommands = new HashMap<>();

    Instance(final T object, final Class<K> methodParamClass) {
        this.object = object;
        this.instanceClass = object.getClass();
        for (Method method : this.object.getClass().getDeclaredMethods()) {
            if (method.getAnnotation(Executable.class) != null) {
                this.subCommands.put(method.getName().toLowerCase(), new SubCommand<>(object, method.getName(), Arrays.asList(method.getParameterTypes()).contains(methodParamClass)));
            }
        }
    }

    Object execute(final String command, final K methodParam, final Object... params) throws WrongNumberOfParams, WrongParams, SubCommandNotFound {
        SubCommand<T, K> subCommand = this.subCommands.get(command.toLowerCase());
        if (subCommand != null) {
            List<Object> parameters = Util.parsed(params);
            return subCommand.invoke(methodParam, parameters.toArray());
        }
        throw new SubCommandNotFound();
    }

    public T getObject() {
        return object;
    }

    Class<?> getInstanceClass() {
        return instanceClass;
    }

    @Override
    public String toString() {
        return "{Instance<" + object.getClass() + ">}";
    }
}
