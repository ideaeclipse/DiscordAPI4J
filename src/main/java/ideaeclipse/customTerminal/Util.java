package ideaeclipse.customTerminal;

import java.lang.reflect.Executable;

/**
 * Util for commands, used to make help menus not ugly
 *
 * @param <K> optional method type
 * @author Ideaclipse
 */
final class Util<K> {
    private final Class<K> methodClass;

    /**
     * @param mc method type
     */
    Util(final Class<K> mc) {
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
            if (!c.equals(methodClass))
                builder.append(c.getSimpleName()).append(",");
        }
        if (builder.charAt(builder.length() - 1) == ',')
            builder.setLength(builder.length() - 1);
        builder.append(">");
        return String.valueOf(builder);
    }

}
