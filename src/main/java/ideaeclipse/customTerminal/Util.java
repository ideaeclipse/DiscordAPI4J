package ideaeclipse.customTerminal;

import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Util for commands, used to make help menus not ugly
 *
 * @param <T> optional constructor type
 * @param <K> optional method type
 * @author Ideaclipse
 */
final class Util<T, K> {
    private final Class<T> constructorClass;
    private final Class<K> methodClass;

    /**
     * @param cc constructor type
     * @param mc method type
     */
    Util(final Class<T> cc, final Class<K> mc) {
        this.constructorClass = cc;
        this.methodClass = mc;
    }

    /**
     * @param executable constructor or method object
     * @return beautified name
     */
    String beautifyConstructor(final Executable executable) {
        StringBuilder builder = new StringBuilder();
        if (!executable.getName().contains("."))
            builder.append(executable.getName());
        builder.append(" <");
        for (Class<?> c : executable.getParameterTypes()) {
            if (!c.equals(constructorClass) && !c.equals(methodClass))
                builder.append(c.getSimpleName()).append(",");
        }
        if (builder.charAt(builder.length() - 1) == ',')
            builder.setLength(builder.length() - 1);
        builder.append(">");
        return String.valueOf(builder);
    }

}
