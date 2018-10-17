package ideaeclipse.DiscordAPI.terminal.listener.Commands;

import ideaeclipse.DiscordAPI.terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.CustomAnnotation;
import ideaeclipse.DiscordAPI.listener.TerminalEvent;
import ideaeclipse.DiscordAPI.utils.DiscordUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This class is used for when a user has entered a custom function and calls the 'help' function
 *
 * @author ideaeclipse
 */
public class ClassInfo extends TerminalEvent {
    private Constructor[] c;
    private Method[] m;
    private Class<?> d, a;
    private Terminal t;

    /**
     * @param t            current terminal instance
     * @param constructors all constructors of the function
     * @param methods      all methods of the function
     * @param def          default class
     * @param admin        admin class
     */
    public ClassInfo(final Terminal t, final Constructor[] constructors, final Method[] methods, final Class<?> def, final Class<?> admin) {
        super(t);
        this.t = t;
        this.c = constructors;
        this.m = methods;
        this.d = def;
        this.a = admin;
    }

    /**
     * @return returns a string composed of all the data of the function
     */
    public String getReturn() {
        StringBuilder string = new StringBuilder();
        string.append("***Function info***").append("\n");
        string.append("Constructors:").append("\n");
        for (Constructor a : c) {
            string.append("    -> ").append(a.getName()).append(" ").append(DiscordUtils.CustomTerminal.convert(a.getParameterTypes())).append("\n");
        }
        string.append("Methods:").append("\n");
        for (Method a : m) {
            if (a.getModifiers() == Modifier.PUBLIC) {
                if (a.getAnnotation(CustomAnnotation.class) != null) {
                    string.append("    -> ").append(a.getName()).append(" ").append(DiscordUtils.CustomTerminal.convert(a.getParameterTypes())).append(" ").append(a.getAnnotation(CustomAnnotation.class).description()).append("\n");
                } else {
                    string.append("    -> ").append(a.getName()).append(" ").append(DiscordUtils.CustomTerminal.convert(a.getParameterTypes())).append(" ").append("\n");
                }
            }
        }
        string = genericCommands(string, t, d, a);
        return String.valueOf(string);
    }

    /**
     * Adds all generic class info
     *
     * @param string current string builder
     * @param t      current terminal instances
     * @param d2     default class
     * @param a2     admin class
     * @return returns an updated string builder
     */
    static StringBuilder genericCommands(final StringBuilder string, final Terminal t, final Class<?> d2, final Class<?> a2) {
        string.append("***Default commands***").append("\n");
        for (Method m : d2.getDeclaredMethods()) {
            string.append("    -> ").append(m.getName().toLowerCase()).append(" ").append(DiscordUtils.CustomTerminal.convert(m.getParameterTypes())).append("\n");
        }
        if (t.isAdmin()) {
            string.append("***Admin Commands***").append("\n");
            for (Method m : a2.getDeclaredMethods()) {
                string.append("    -> ").append(m.getName().toLowerCase()).append(" ").append(DiscordUtils.CustomTerminal.convert(m.getParameterTypes())).append("\n");
            }
        }
        return string;
    }
}
