package DiscordAPI.Terminal.logic;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.terminalListener.listenerTypes.Commands.ExitingFunction;
import DiscordAPI.listener.terminalListener.listenerTypes.Commands.NoSuchMethod;
import DiscordAPI.listener.terminalListener.listenerTypes.Commands.ProgramReturnValues;
import DiscordAPI.listener.terminalListener.listenerTypes.errors.WrongNumberOfArgs;
import DiscordAPI.listener.terminalListener.listenerTypes.errors.WrongType;
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
public class Execute {
    private static final DiscordLogger LOGGER = new DiscordLogger(Execute.class.getName());
    private static final String fmt = "%24s: %s%n";
    private Terminal t;
    private Class<?> calledClass;
    private Object ob;

    public Execute(String file, List<String> input, Terminal Terminal) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        t = Terminal;
        calledClass = Class.forName(file);
        if (input.size() > 2) {
            LOGGER.info("Looking for valid constructor formats");
            method(constructor(calledClass, (ArrayList<String>) input), calledClass);
        } else {
            LOGGER.info("Executing blank function");
            ob = calledClass.newInstance();
            method(ob, calledClass);
        }
    }

    private Object constructor(Class<?> c, ArrayList<String> input) throws IllegalAccessException, InvocationTargetException, InstantiationException {
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

    public void method(Object ob, Class<?> c) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ArrayList<String> input = getInput();
        LOGGER.info("Input: " + input);
        Class<?>[] paramTypes = getParameters(c, input.get(0));
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
                Method method = ob.getClass().getMethod(input.get(0));
                Object o = method.invoke(ob);
                if (o != null) {
                    t.getDispatcher().notify(new ProgramReturnValues(t, o.toString()));
                }
            }
        } else if (input.get(0).equals("help")) {
            Compare.getClassData(c, t);
        } else if (input.get(0).equals("done")) {
            t.changeStatus(false);
            t.getDispatcher().notify(new ExitingFunction(t));
            Method m = ob.getClass().getMethod("done");
            m.invoke(ob);
        }
    }

    private ArrayList<String> getInput() {
        return t.getAdditionalInput();
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

    private Object[] convertArgs(ArrayList<String> input, Class<?>[] params) {
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

    public Class<?> getCalledClass() {
        return this.calledClass;
    }

    public Object getObject() {
        return this.ob;
    }

    public Execute(Terminal t) {
        this.t = t;
    }

    public Object invoke(Class<?> calledClass, ArrayList<String> words) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (words.size() >= 1) {
            return test(calledClass, words);
        }
        return null;
    }

    private Object test(Class<?> c, ArrayList<String> words) throws IllegalAccessException, InvocationTargetException, InstantiationException, IndexOutOfBoundsException {
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