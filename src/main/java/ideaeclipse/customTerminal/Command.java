package ideaeclipse.customTerminal;

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
 * @param <T> Comparator and instance map key
 * @param <K> Method param type
 * @author Ideaeclipse
 */
class Command<T, K> {
    private final T object;
    private final Method method;
    private final boolean needsParam;

    /**
     * @param object     Comparator object
     * @param methodName methodName, will search for the method object
     * @param needsParam if the method needs the method param to be passed
     */
    Command(final T object, final String methodName, final boolean needsParam) {
        this.object = object;
        this.method = Arrays.stream(object.getClass().getDeclaredMethods()).filter(o -> o.getName().toLowerCase().equals(methodName.toLowerCase())).collect(Collectors.toList()).get(0);
        this.method.setAccessible(true);
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
        List<Object> objects = new LinkedList<>(Arrays.asList(params));
        if (needsParam)
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
            throw new WrongNumberOfParams(Arrays.asList(params).size(), needsParam ? method.getParameterCount() - 1 : method.getParameterCount());
        }
    }

    /**
     * @return method object
     */
    Method getMethod() {
        return this.method;
    }
}
