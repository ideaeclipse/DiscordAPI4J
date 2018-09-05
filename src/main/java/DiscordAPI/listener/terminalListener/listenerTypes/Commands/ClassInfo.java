package DiscordAPI.listener.terminalListener.listenerTypes.Commands;

import DiscordAPI.Terminal.NameConversion;
import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.CustomAnnotation;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.ListenerFeatures;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassInfo extends ListenerEvent implements ListenerFeatures {
    private Constructor[] c;
    private Method[] m;
    private Class<?> d, a;
    private Terminal t;

    private ClassInfo(Terminal t) {
        super(t);
    }

    public ClassInfo(Terminal t, Constructor[] constructors, Method[] methods, Class<?> def, Class<?> admin) {
        this(t);
        this.t = t;
        this.c = constructors;
        this.m = methods;
        this.d = def;
        this.a = admin;
    }

    @Override
    public String getReturn() {
        StringBuilder string = new StringBuilder();
        string.append("***Function info***").append("\n");
        string.append("Constructors:").append("\n");
        for (Constructor a : c) {
            string.append("    -> ").append(a.getName()).append(" ").append(NameConversion.convert(a.getParameterTypes())).append("\n");
        }
        string.append("Methods:").append("\n");
        for (Method a : m) {
            if (a.getModifiers() == Modifier.PUBLIC) {
                if (a.getAnnotation(CustomAnnotation.class) != null) {
                    string.append("    -> ").append(a.getName()).append(" ").append(NameConversion.convert(a.getParameterTypes())).append(" ").append(a.getAnnotation(CustomAnnotation.class).description()).append("\n");
                } else {
                    string.append("    -> ").append(a.getName()).append(" ").append(NameConversion.convert(a.getParameterTypes())).append(" ").append("\n");
                }
            }
        }
        string = genericCommands(string, t, d, a);
        return String.valueOf(string);
    }

    static StringBuilder genericCommands(StringBuilder string, Terminal t, Class<?> d2, Class<?> a2) {
        string.append("***Default commands***").append("\n");
        for (Method m : d2.getDeclaredMethods()) {
            string.append("    -> ").append(m.getName().toLowerCase()).append(" ").append(NameConversion.convert(m.getParameterTypes())).append("\n");
        }
        if (t.isAdmin()) {
            string.append("***Admin Commands***").append("\n");
            for (Method m : a2.getDeclaredMethods()) {
                string.append("    -> ").append(m.getName().toLowerCase()).append(" ").append(NameConversion.convert(m.getParameterTypes())).append("\n");
            }
        }
        return string;
    }
}
