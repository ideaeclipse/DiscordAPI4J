package ideaeclipse.DiscordAPI.Terminal;

import ideaeclipse.AsyncUtility.AsyncList;
import ideaeclipse.AsyncUtility.ForEachList;
import ideaeclipse.DiscordAPI.listener.terminalListener.Commands.ClassInfo;
import ideaeclipse.DiscordAPI.listener.terminalListener.Commands.ExitingFunction;
import ideaeclipse.DiscordAPI.listener.terminalListener.Commands.NoSuchMethod;
import ideaeclipse.DiscordAPI.listener.terminalListener.Commands.ProgramReturnValues;
import ideaeclipse.DiscordAPI.listener.terminalListener.errors.WrongNumberOfArgs;
import ideaeclipse.DiscordAPI.listener.terminalListener.errors.WrongType;
import ideaeclipse.DiscordAPI.utils.DiscordLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * This file instantiates the functions/executes the specified method
 *
 * @author ideaeclipse
 */
class Execute {
    private static final DiscordLogger LOGGER = new DiscordLogger(Execute.class.getName());
    private final Terminal t;
    private Class<?> calledClass, defaultClass;
    private Object ob;

    /**
     * @param file         the function name
     * @param input        all words inputted
     * @param terminal     current terminal
     * @param defaultClass default class
     * @param adminClass   admin class
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
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

    /**
     * This method allows users to instantiate an object that has parameters inside a constructor
     *
     * @param c     class
     * @param input inputted words
     * @return returns an object
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
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

    /**
     * @param ob           functions instance
     * @param c            function class
     * @param defaultClass default class
     * @param adminClass   admin class
     */
    public void method(final Object ob, final Class<?> c, final Class<?> defaultClass, final Class<?> adminClass) {
        ArrayList<String> input = getInput();
        LOGGER.info("Input: " + input);
        Class<?>[] paramTypes = getParameters(c, input.get(0));
        AsyncList<?> list = new ForEachList<>();
        list.add(x -> {
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
                                    t.getDispatcher().callEvent(new ProgramReturnValues(t, o.toString()));
                                }
                            }
                        } else {
                            t.getDispatcher().callEvent(new WrongNumberOfArgs(t, paramTypes));
                        }
                    } else {
                        try {
                            Method method = ob.getClass().getDeclaredMethod(input.get(0));
                            Object o = method.invoke(ob);
                            if (o != null) {
                                t.getDispatcher().callEvent(new ProgramReturnValues(t, o.toString()));
                            }
                        } catch (NoSuchMethodException e) {
                            Object o = checkDefault(input);
                            if (o != null) {
                                t.getDispatcher().callEvent(new ProgramReturnValues(t, (String) o));
                            } else {
                                t.getDispatcher().callEvent(new NoSuchMethod(t));
                            }
                        }

                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        }).add(x -> {
            if (input.size() > 0) {
                if (input.get(0).equals("help")) {
                    t.getDispatcher().callEvent(new ClassInfo(t, c.getConstructors(), c.getDeclaredMethods(), defaultClass, adminClass));
                }
            }
            return Optional.empty();
        }).add(x -> {
            try {
                if (input.size() > 0) {
                    if (input.get(0).equals("done")) {
                        t.changeStatus(false);
                        t.getDispatcher().callEvent(new ExitingFunction(t));
                        if (ob != null) {
                            Method m = ob.getClass().getMethod("done");
                            m.invoke(ob);
                        }
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        });
        if (input.size() > 0) {
            list.execute();
        }
    }

    private ArrayList<String> getInput() {
        return t.getAdditionalInput();
    }

    /**
     * checks if the command is a default command
     *
     * @param words all words
     * @return returns an instance of the default command
     */
    private Object checkDefault(final ArrayList<String> words) {
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

    /**
     * @param c      class
     * @param method method name
     * @return array of parameter types
     */
    private Class<?>[] getParameters(final Class<?> c, final String method) {
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

    /**
     * @param input  words
     * @param params parameter types
     * @return converted
     */
    private Object[] convertArgs(final List<String> input, final Class<?>[] params) {
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
        t.getDispatcher().callEvent(new WrongType(t));
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
                t.getDispatcher().callEvent(new WrongNumberOfArgs(t, m.getParameterTypes()));
                break;
            } else {
                count++;
            }
        }
        return null;
    }
}