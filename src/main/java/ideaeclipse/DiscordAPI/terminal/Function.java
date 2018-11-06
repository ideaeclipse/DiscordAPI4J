package ideaeclipse.DiscordAPI.terminal;

import ideaeclipse.AsyncUtility.Async;
import ideaeclipse.DiscordAPI.terminal.listener.Commands.*;
import ideaeclipse.DiscordAPI.terminal.listener.errors.WrongNumberOfArgs;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class deals with function instances
 * All function instances and methods get called from inputhandler
 *
 * @author ideaeclipse
 * @see InputHandler
 */
class Function {
    private final Terminal t;
    private Class<?> c, a, d;
    private Object object;

    /**
     * @param c function class
     * @param t current terminal session
     */
    Function(final Class<?> c, final Terminal t) {
        this.t = t;
        setGeneric();
        this.c = c;
        try {
            this.object = c.getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        t.getDispatcher().callEvent(new EnteringFunction(t, c.getSimpleName()));
    }

    /**
     * @param c      function class
     * @param t      current terminal
     * @param params constructor parameters
     */
    Function(Class<?> c, Terminal t, List<String> params) {
        this.t = t;
        setGeneric();
        this.c = c;
        Constructor con = Arrays
                .stream(c.getDeclaredConstructors())
                .filter(o -> o.getParameterTypes().length == params.size())
                .collect(Collectors.toList()).get(0);
        if (con.getParameterTypes().length == params.size()) {
            try {
                this.object = con.newInstance(convertArgs(params, con.getParameterTypes(), t));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        t.getDispatcher().callEvent(new EnteringFunction(t, c.getSimpleName()));
    }

    private void setGeneric() {
        this.a = t.getCommandList().getGenericCommandMap().get("adminCommands").get(0);
        this.d = t.getCommandList().getGenericCommandMap().get("defaultCommands").get(0);
    }

    /**
     * This method will execute whatever params you send it
     * This will execute generic,help,exit and dynamic methods
     *
     * @param params list of method params
     */
    void executeMethod(List<String> params) {
        if (params.size() >= 1) {
            switch (params.get(0)) {
                case "help":
                    t.getDispatcher().callEvent(new ClassInfo(t, c.getDeclaredConstructors(), c.getDeclaredMethods(), d, a));
                    break;
                case "done":
                    t.changeStatus(false);
                    t.getDispatcher().callEvent(new ExitingFunction(t));
                    break;
                default:
                    //none generic
                    if (!generic.isGenericCommand(params.get(0), a, d)) {
                        Method m = Arrays
                                .stream(this.c.getDeclaredMethods())
                                .filter(o -> o.getName().toLowerCase().trim().equals(params.get(0).toLowerCase().trim()) && o.getParameterTypes().length == params.size() - 1)
                                .collect(Collectors.toList()).get(0);
                        invoke(this.object, params, m, t);
                    } else {
                        generic.invoke(t, params, a, d);
                    }
                    break;
            }
        }
    }

    /**
     * @param object class instance
     * @param params params
     * @param m      method you whish to envoke
     */
    private static void invoke(final Object object, final List<String> params, final Method m, final Terminal t) {
        Async.blankThread(() -> {
            params.remove(0);
            try {
                Object[] convertedArgs = convertArgs(params, m.getParameterTypes(), t);
                if (convertedArgs != null) {
                    Object r = m.invoke(object, convertArgs(params, m.getParameterTypes(), t));
                    if (r != null)
                        t.getDispatcher().callEvent(new ProgramReturnValues(t, String.valueOf(r)));
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * @param input  params you wish to convert
     * @param params param types you want to convert each index to
     * @return a array of converted params
     */
    private static Object[] convertArgs(final List<String> input, final Class<?>[] params, final Terminal t) {
        List<Object> args = new LinkedList<>();
        for (int i = 0; i < params.length; i++) {
            if (params[i] == String.class) {
                args.add(input.get(i));
            } else if (params[i] == int.class) {
                args.add(Integer.parseInt(input.get(i)));
            } else if (params[i] == float.class) {
                try {
                    args.add(Float.parseFloat(input.get(i)));
                } catch (NumberFormatException e) {
                    args = null;
                    break;
                }
            } else if (params[i] == Long.class) {
                args.add(Long.parseUnsignedLong(input.get(i).substring(2, input.get(i).length() - 1)));
            }
        }
        if (args == null) {
            t.getDispatcher().callEvent(new WrongNumberOfArgs(t, params));
            return null;
        }
        return Objects.requireNonNull(args).toArray();
    }

    /**
     * This class deals with all things related to generic commands
     *
     * @author ideaeclipse
     * @see CommandList
     */
    static class generic {
        /**
         * @param t      terminal session
         * @param params inputted words
         * @param a      admin class
         * @param d      default class
         */
        static void invoke(final Terminal t, final List<String> params, final Class<?> a, final Class<?> d) {
            Async.blankThread(() -> {
                if (t.isAdmin()) {
                    List<Method> methods = Arrays
                            .stream(a.getDeclaredMethods())
                            .filter(o -> o.getName().toLowerCase().trim().equals(params.get(0).toLowerCase().trim()))
                            .collect(Collectors.toList());
                    if (methods.size() > 0) {
                        try {
                            Function.invoke(a.getConstructors()[0].newInstance(t), params, methods.get(0), t);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (params.size() >= 1) {
                    List<Method> methods = Arrays
                            .stream(d.getDeclaredMethods())
                            .filter(o -> o.getName().toLowerCase().trim().equals(params.get(0).toLowerCase().trim()))
                            .collect(Collectors.toList());
                    if (methods.size() > 0) {
                        try {
                            Function.invoke(d.getConstructors()[0].newInstance(t), params, methods.get(0), t);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        /**
         * @param methodName method name you wish to check
         * @return if it is a generic method or nto
         */
        static boolean isGenericCommand(String methodName, Class<?> a, Class<?> d) {
            return (Arrays
                    .stream(a.getDeclaredMethods())
                    .filter(o -> o.getName().toLowerCase().trim().equals(methodName.toLowerCase().trim()))
                    .collect(Collectors.toList())
                    .size() > 0)
                    || (Arrays
                    .stream(d.getDeclaredMethods())
                    .filter(o -> o.getName().toLowerCase().trim().equals(methodName.toLowerCase().trim()))
                    .collect(Collectors.toList())
                    .size() > 0);
        }
    }
}
