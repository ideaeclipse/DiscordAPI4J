package ideaeclipse.customTerminal;

import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.customTerminal.exceptions.WrongNumberOfParams;
import ideaeclipse.customTerminal.exceptions.WrongParams;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Command object
 *
 * @param <K> Method param type
 * @author Ideaeclipse
 */
class Command<K> {
    private final IDiscordBot bot;
    private final CommandsClass commandsClass;
    private final Method method;
    private final boolean needsBot;
    private final boolean needsParam;

    /**
     * @param bot           to pass to method if required
     * @param commandsClass commandsClass object
     * @param methodName    methodName, will search for the method object
     * @param needsParam    if the method needs the method param to be passed
     */
    Command(final IDiscordBot bot, final CommandsClass commandsClass, final String methodName, final boolean needsBot, final boolean needsParam) {
        this.bot = bot;
        this.commandsClass = commandsClass;
        this.method = Arrays.stream(commandsClass.getClass().getDeclaredMethods()).filter(o -> o.getName().toLowerCase().equals(methodName.toLowerCase())).collect(Collectors.toList()).get(0);
        this.method.setAccessible(true);
        this.needsBot = needsBot;
        this.needsParam = needsParam;
    }

    /**
     * invokes the method with params you passed, also with the optional method param if the method requires it
     *
     * @param methodParam optional method param
     * @param params      other params that can be passed inside a string, i.e ints, string, long, double
     * @return whatever the method returned, should always be a primitive type or a string
     * @throws WrongNumberOfParams when you passed x number of params but they expected y
     * @throws WrongParams         when you passed a string but they want an int for example.
     */
    Object invoke(final K methodParam, final Object... params) throws WrongNumberOfParams, WrongParams {
        List<Object> objects = new LinkedList<>();
        if (needsBot)
            objects.add(0, this.bot);
        if (needsParam)
            objects.add(getParamIndex(methodParam), methodParam);
        objects.addAll(Arrays.asList(params));
        if (method.getParameterCount() == objects.size()) {
            try {
                return this.method.invoke(this.commandsClass, objects.toArray());
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                e.printStackTrace();
                List<Class<?>> classList = new LinkedList<>();
                Arrays.stream(params).forEach(o -> classList.add(o.getClass()));
                throw new WrongParams(classList, Arrays.asList(method.getParameterTypes()));
            }
        } else {
            int size = method.getParameterCount();
            if (needsBot)
                size -= 1;
            if (needsParam)
                size -= 1;
            throw new WrongNumberOfParams(Arrays.asList(params).size(), size);
        }
    }

    /**
     * @return method object
     */
    Method getMethod() {
        return this.method;
    }

    /**
     * Checks to see where the methodParam should be placed, before or after the bot object if present
     *
     * @param methodParam method param object
     * @return index to place
     */
    private int getParamIndex(final K methodParam) {
        List<Class<?>> paramTypes = Arrays.asList(this.method.getParameterTypes());
        int initial = paramTypes.indexOf(methodParam.getClass());
        if (initial == -1) {
            for (Class<?> c : methodParam.getClass().getInterfaces()) {
                if (paramTypes.contains(c)) {
                    initial = paramTypes.indexOf(c);
                }
            }
            if (initial == -1)
                if (paramTypes.contains(methodParam.getClass().getSuperclass())) {
                    initial = paramTypes.indexOf(methodParam.getClass().getSuperclass());
                }
        }
        return initial;
    }
}
