package DiscordAPI.Terminal;

import DiscordAPI.listener.terminalListener.listenerTypes.Commands.ClassInfo;
import DiscordAPI.listener.terminalListener.listenerTypes.Commands.ExitingFunction;
import DiscordAPI.listener.terminalListener.listenerTypes.Commands.NoSuchMethod;
import DiscordAPI.listener.terminalListener.listenerTypes.Commands.ProgramReturnValues;
import DiscordAPI.listener.terminalListener.listenerTypes.errors.WrongNumberOfArgs;
import DiscordAPI.listener.terminalListener.listenerTypes.errors.WrongType;
import DiscordAPI.utils.Async;
import DiscordAPI.utils.DiscordLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/*
 * In your methods you must have it to be public in order to be called
 * They all must either return string or void
 */
class Execute {
    private static final DiscordLogger LOGGER = new DiscordLogger(Execute.class.getName());
    private final Terminal t;
    private Class<?> calledClass, defaultClass;
    private Object ob;

    Execute(final String file, final List<String> input, final Terminal terminal, final Class<?> defaultClass, final Class<?> adminClass) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        t = terminal;
        calledClass = Class.forName(file);
        this.defaultClass = defaultClass;
        if (input.size() > 2) {
            LOGGER.info("Looking for valid constructor formats");
            method(constructor(calledClass, input), calledClass, defaultClass, adminClass);
        } else {
            LOGGER.info("Executing blank function");
            ob = calledClass.getDeclaredConstructors()[0].newInstance();
            method(ob, calledClass, defaultClass, adminClass);
        }
    }

    private Object constructor(final Class<?> c, final List<String> input) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?>[] constructor = c.getConstructors();
        input.remove(0);
        input.remove(0);
        Class<?>[] params;
        Object ob = null;
        for (Constructor con : constructor) {
            if (con.getParameterTypes().length == input.size()) {
                params = con.getParameterTypes();
                ob = con.newInstance(convertArgs(input, params));
                break;
            }
        }
        return ob;
    }

    public void method(final Object ob, final Class<?> c, final Class<?> defaultClass, final Class<?> adminClass) {
        ArrayList<String> input = getInput();
        LOGGER.info("Input: " + input);
        Class<?>[] paramTypes = getParameters(c, input.get(0));
        Async.AsyncList<?> list = new Async.AsyncList<>();
        list.add(() -> {
            try {
                if (!input.get(0).equals("done") && !input.get(0).equals("help")) {
                    if (paramTypes.length > 0) {
                        LOGGER.info("Params: " + Arrays.toString(paramTypes));
                        Method method = ob.getClass().getMethod(input.get(0), paramTypes);
                        input.remove(0);
                        if (input.size() == paramTypes.length) {
                            Object[] convertedArgs = convertArgs(input, paramTypes);
                            if (convertedArgs != null) {
                                LOGGER.info("Converted Args: " + Arrays.toString(convertedArgs));
                                Object o = method.invoke(ob, convertedArgs);
                                if (o != null) {
                                    t.getDispatcher().notify(new ProgramReturnValues(t, o.toString()));
                                }
                            }
                        } else {
                            t.getDispatcher().notify(new WrongNumberOfArgs(t, paramTypes));
                        }
                    } else {
                        try {
                            Method method = ob.getClass().getMethod(input.get(0));
                            Object o = method.invoke(ob);
                            if (o != null) {
                                t.getDispatcher().notify(new ProgramReturnValues(t, o.toString()));
                            }
                        } catch (NoSuchMethodException ignored) {
                            Object o = checkDefault(input);
                            if (o != null) {
                                t.getDispatcher().notify(new ProgramReturnValues(t, (String) o));
                            } else {
                                t.getDispatcher().notify(new NoSuchMethod(t));
                            }
                        }

                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }).add(() -> {
            if (input.get(0).equals("help")) {
                t.getDispatcher().notify(new ClassInfo(t, c.getConstructors(), c.getDeclaredMethods(), defaultClass, adminClass));
            }
            return null;
        }).add(() -> {
            try {
                if (input.get(0).equals("done")) {
                    t.changeStatus(false);
                    t.getDispatcher().notify(new ExitingFunction(t));
                    if (ob != null) {
                        Method m = ob.getClass().getMethod("done");
                        m.invoke(ob);
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        });
        if (input.size() > 0)
            Async.execute(list);
    }

    private ArrayList<String> getInput() {
        return t.getAdditionalInput();
    }

    private Object checkDefault(ArrayList<String> words) {
        LOGGER.info("CHECK default");
        Execute m = new Execute(t);
        Object o = null;
        try {
            o = m.invoke(defaultClass, words);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e1) {
            e1.printStackTrace();
        }
        return o;
    }

    private Class<?>[] getParameters(Class<?> c, String method) {
        Method[] allMethods = c.getDeclaredMethods();
        for (Method m : allMethods) {
            if (m.getModifiers() == Modifier.PUBLIC) {
                if (!m.getName().equals(method)) {
                    continue;
                }
            }
            return m.getParameterTypes();

        }
        return new Class[0];
    }

    private Object[] convertArgs(List<String> input, Class<?>[] params) {
        Object[] args = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            if (params[i] == String.class) {
                args[i] = input.get(i);
            } else if (params[i] == int.class) {
                args[i] = Integer.parseInt(input.get(i));
            } else if (params[i] == float.class) {
                try {
                    args[i] = Float.parseFloat(input.get(i));
                } catch (NumberFormatException e) {
                    wrongTypeException();
                    args = null;
                    break;
                }
            } else if (params[i] == Long.class) {
                args[i] = Long.parseUnsignedLong(input.get(i).substring(2, input.get(i).length() - 1));
            }
        }
        return args;
    }

    private void wrongTypeException() {
        t.getDispatcher().notify(new WrongType(t));
    }

    Class<?> getCalledClass() {
        return this.calledClass;
    }

    public Object getObject() {
        return this.ob;
    }

    Execute(Terminal t) {
        this.t = t;
    }

    Object invoke(Class<?> calledClass, ArrayList<String> words) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (words.size() >= 1) {
            return invokeClass(calledClass, words);
        }
        return null;
    }

    private Object invokeClass(Class<?> c, ArrayList<String> words) throws IllegalAccessException, InvocationTargetException, InstantiationException, IndexOutOfBoundsException {
        int count = 0;
        for (Method m : c.getDeclaredMethods()) {
            if (words.get(0).toLowerCase().equals(m.getName().toLowerCase())) {
                words.remove(0);
                if (words.size() == m.getParameterTypes().length) {
                    return m.invoke(c.getConstructors()[0].newInstance(t), convertArgs(words, m.getParameterTypes()));
                }
                t.getDispatcher().notify(new WrongNumberOfArgs(t, m.getParameterTypes()));
                break;
            } else {
                count++;
            }
        }
        if (count == c.getDeclaredMethods().length) {
            t.getDispatcher().notify(new NoSuchMethod(t));
        }
        return null;
    }
}