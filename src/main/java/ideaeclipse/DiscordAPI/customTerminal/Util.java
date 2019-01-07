package ideaeclipse.DiscordAPI.customTerminal;

import java.lang.reflect.Executable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

final class Util<T, K> {
    private final Class<T> constructorClass;
    private final Class<K> methodClass;

    public Util(final Class<T> cc, final Class<K> mc) {
        this.constructorClass = cc;
        this.methodClass = mc;
    }

    String beautifyConstructor(final Executable executable) {
        StringBuilder builder = new StringBuilder();
        if (!executable.getName().contains("."))
            builder.append(executable.getName());
        builder.append("(");
        for (Class<?> c : executable.getParameterTypes()) {
            if (!c.equals(constructorClass) && !c.equals(methodClass))
                builder.append(c.getSimpleName()).append(",");
        }
        if (builder.charAt(builder.length() - 1) == ',')
            builder.setLength(builder.length() - 1);
        builder.append(")");
        return String.valueOf(builder);
    }

    static List<Object> parsed(final Object... params) {
        List<Object> objects = new LinkedList<>(Arrays.asList(params));
        for (int i = 0; i < objects.size(); i++) {
            try {
                objects.set(i, Integer.parseInt(String.valueOf(objects.get(i))));
            } catch (NumberFormatException ignored) {

            }
        }
        return objects;
    }

}
