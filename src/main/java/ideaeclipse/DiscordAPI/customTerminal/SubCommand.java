package ideaeclipse.DiscordAPI.customTerminal;

import ideaeclipse.DiscordAPI.customTerminal.exceptions.WrongNumberOfParams;
import ideaeclipse.DiscordAPI.customTerminal.exceptions.WrongParams;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

class SubCommand<T, K> {
    private final T object;
    private final Method method;
    private final boolean needsMessage;

    SubCommand(final T object, final String
            methodName, final boolean needsMessage) {
        this.object = object;
        this.method = Arrays.stream(object.getClass().getDeclaredMethods()).filter(o -> o.getName().toLowerCase().equals(methodName.toLowerCase())).collect(Collectors.toList()).get(0);
        this.method.setAccessible(true);
        this.needsMessage = needsMessage;
    }

    Object invoke(final K methodParam, final Object... params) throws WrongNumberOfParams, WrongParams {
        List<Object> objects = new LinkedList<>(Arrays.asList(params));
        if (needsMessage)
            objects.add(0, methodParam);
        if (method.getParameterCount() == objects.size()) {
            try {
                return this.method.invoke(this.object, objects.toArray());
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                e.printStackTrace();
                List<Class<?>> classList = new LinkedList<>();
                Arrays.stream(params).forEach(o -> classList.add(o.getClass()));
                throw new WrongParams(classList, Arrays.asList(method.getParameterTypes()));
            }
        } else {
            throw new WrongNumberOfParams(Arrays.asList(params).size(), method.getParameterCount());
        }
    }

    public Method getMethod() {
        return method;
    }
}
