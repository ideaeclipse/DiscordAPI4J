package ideaeclipse.DiscordAPI.customTerminal;

import ideaeclipse.DiscordAPI.customTerminal.exceptions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class CustomTerminal<T, K> {
    private final CommandList list;
    private final T constructorParam;
    private final Class<K> methodParamClass;
    private final Util<?, K> util;

    CustomTerminal(final String commands, final String generic, final T constructorParam, final Class<K> methodParamClass) {
        this.list = new CommandList(commands, generic);
        this.constructorParam = constructorParam;
        this.methodParamClass = methodParamClass;
        Class<?>[] interfaces = constructorParam.getClass().getInterfaces();
        this.util = new Util<>(interfaces.length > 0 ? interfaces[0] : null, methodParamClass);
    }

    Instance<?, K> newInstance(final List<String> input, final Object... params) throws ImproperCommandFormat, PackageNotFound, SubCommandNotFound, ConstructorNotFound {
        List<Object> param = new LinkedList<>(Arrays.asList(params));
        Class<?> instanceClass = getClassFromInput(input);
        List<Constructor<?>> constructors = Arrays.stream(instanceClass.getConstructors()).filter(o -> {
            if (!Collections.disjoint(Arrays.asList(o.getParameterTypes()), Arrays.asList(constructorParam.getClass().getInterfaces())))
                param.add(0, this.constructorParam);
            return o.getParameterCount() == param.size();
        }).collect(Collectors.toList());
        Constructor<?> constructor;
        if (!constructors.isEmpty()) {
            constructor = constructors.get(0);
            constructor.setAccessible(true);
        } else {
            List<List<Class<?>>> options = new LinkedList<>();
            for (Constructor<?> constructor1 : instanceClass.getDeclaredConstructors())
                options.add(Arrays.asList(constructor1.getParameterTypes()));
            throw new ConstructorNotFound(input.get(1), options);
        }
        try {
            return new Instance<>(constructor.newInstance(Util.parsed(params).toArray()), methodParamClass);
        } catch (IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            List<List<Class<?>>> options = new LinkedList<>();
            for (Constructor<?> constructor1 : instanceClass.getDeclaredConstructors())
                options.add(Arrays.asList(constructor1.getParameterTypes()));
            throw new ConstructorNotFound(input.get(1), options);
        }

    }

    String help(final Instance<?, K> instance) {
        return printHelp(instance.getInstanceClass());
    }

    String help(final List<String> input, final Map<String, SubCommand<?, K>> genericCommands, final boolean useInstance) throws SubCommandNotFound, PackageNotFound, ImproperCommandFormat, InvalidHelpFormat {
        switch (input.size()) {
            case 1:
                StringBuilder builder = new StringBuilder();
                if (useInstance) {
                    builder.append("Instances\n");
                    for (Map.Entry<String, List<Class<?>>> entry : list.getCommandMap().entrySet()) {
                        builder.append("    ").append(entry.getKey()).append(":\n");
                        for (Class<?> c : entry.getValue()) {
                            builder.append("        -> ").append(c.getSimpleName()).append("\n");
                        }
                    }
                }
                builder.append("Commands\n");
                for (Map.Entry<String, SubCommand<?, K>> entry : genericCommands.entrySet()) {
                    builder.append("    -> ").append(this.util.beautifyConstructor(entry.getValue().getMethod())).append("\n");
                }
                return String.valueOf(builder);
            case 3:
                List<String> temp = new LinkedList<>(input);
                temp.remove(0);
                Class<?> foundClass = getClassFromInput(temp);
                if (foundClass == null)
                    throw new SubCommandNotFound();
                else
                    return printHelp(foundClass);
            default:
                throw new InvalidHelpFormat();
        }
    }

    CommandList getList() {
        return list;
    }

    private String printHelp(final Class<?> foundClass) {
        StringBuilder help = new StringBuilder();
        help.append("Initializer options:").append("\n");
        for (Constructor constructor : foundClass.getDeclaredConstructors())
            help.append("   -> ").append(this.util.beautifyConstructor(constructor)).append("\n");
        help.append("Instance Commands:").append("\n");
        for (Method method : foundClass.getDeclaredMethods())
            help.append("   -> ").append(this.util.beautifyConstructor(method)).append("\n");
        return String.valueOf(help);
    }

    private Class<?> getClassFromInput(final List<String> input) throws PackageNotFound, ImproperCommandFormat, SubCommandNotFound {
        final Map<String, List<Class<?>>> commands = this.list.getCommandMap();
        if (input.size() == 2) {
            if (commands.containsKey(input.get(0))) {
                List<Class<?>> classes = commands.get(input.get(0)).stream().filter(o -> o.getSimpleName().toLowerCase().equals(input.get(1).toLowerCase())).collect(Collectors.toList());
                if (!classes.isEmpty())
                    return classes.get(0);
                else
                    throw new SubCommandNotFound();
            } else
                throw new PackageNotFound();
        } else
            throw new ImproperCommandFormat();
    }

}
