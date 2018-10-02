package ideaeclipse.DiscordAPI.Terminal;

import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.Commands.BotCommands;
import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.Commands.ClassInfo;
import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.Commands.EnteringFunction;
import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.Commands.ProgramReturnValues;
import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.errors.InvalidArgument;
import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.errors.InvalidCommand;
import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.errors.InvalidHelpFormat;
import ideaeclipse.DiscordAPI.utils.DiscordLogger;
import ideaeclipse.AsyncUtility.Async;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * This file is the entire logic of what to do once a user is inside a function
 *
 * @author ideaeclipse
 */
class Compare {
    private static final DiscordLogger LOGGER = new DiscordLogger(Compare.class.getName());
    private HashMap commands;
    private HashMap commandMethods;
    private static Class<?> defaultClass;
    private static Class<?> adminClass;
    private ArrayList<String> words;
    private Terminal t;
    private List methods;
    private int index;

    /**
     * @param input    words inputted
     * @param terminal current terminal
     * @return if there is more required input
     * @throws IOException            permissions
     * @throws ClassNotFoundException if class isn't found
     */
    boolean Initiate(final ArrayList<String> input, final Terminal terminal) throws IOException, ClassNotFoundException {
        t = terminal;
        words = input;
        CommandList cl = new CommandList(t.getBot());
        commands = cl.getCommands();
        commandMethods = cl.getCommandMethods();
        defaultClass = cl.getDefaultCommands();
        adminClass = cl.getAdminCommands();
        return logic();
    }

    /**
     * @return if there is more required input
     */
    private boolean logic() {
        Async.AsyncList<Boolean> genericList = new Async.AsyncList<>();
        genericList.add(() -> {
            if (words.size() > 0) {
                if (checkGeneric(words.get(0), defaultClass.getDeclaredMethods())) {
                    LOGGER.debug("Executing default command");
                    Execute e = new Execute(t);
                    Object o = null;
                    try {
                        o = e.invoke(defaultClass, words);
                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e1) {
                        e1.printStackTrace();
                    }
                    if (o != null) {
                        LOGGER.debug("Default return value: " + o.toString());
                        t.getDispatcher().callEvent(new ProgramReturnValues(t, (String) o));
                    } else {
                        LOGGER.error("Default return value was null");
                    }
                    return true;
                }
            }
            return false;
        }).add(() -> {
            if (words.size() > 0) {
                if (checkGeneric(words.get(0), adminClass.getDeclaredMethods())) {
                    if (t.isAdmin()) {
                        LOGGER.debug("Executing admin command");
                        Object o = null;
                        try {
                            o = executeGeneric(adminClass, words.get(0));
                        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        if (o != null) {
                            LOGGER.debug("Admin return value: " + o.toString());
                            t.getDispatcher().callEvent(new ProgramReturnValues(t, o.toString()));
                        } else {
                            LOGGER.error("Admin return value was null");
                        }
                        return true;
                    }
                }
            }
            return false;
        });
        Async.AsyncList<Boolean> list = new Async.AsyncList<>();
        list.add(() -> {
            if (commands.get(words.get(0)) == null && !words.get(0).equals("help")) {
                t.getDispatcher().callEvent(new InvalidCommand(t));
            }
            return false;
        }).add(() -> {
            if (words.get(0).equals("help")) {
                switch (words.size()) {
                    case 1:
                        CommandList cl = null;
                        try {
                            cl = new CommandList(t.getBot());
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        assert cl != null;
                        Map m = cl.getCommands();
                        List l = new ArrayList(m.keySet());
                        t.getDispatcher().callEvent(new BotCommands(t, l, m, defaultClass, adminClass));
                        break;
                    case 2:
                        t.getDispatcher().callEvent(new InvalidHelpFormat(t, words.get(1), (List<String>) commands.get(words.get(1))));
                        break;
                    case 3:
                        List args = (List) commands.get(words.get(1));
                        List methods = (List) commandMethods.get(words.get(1));
                        int index = getMethod(words, args.iterator(), 2);
                        if (index != -1) {
                            Class<?> c = null;
                            try {
                                c = Class.forName(methods.get(index).toString());
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            assert c != null;
                            t.getDispatcher().callEvent(new ClassInfo(t, c.getConstructors(), c.getDeclaredMethods(), defaultClass, adminClass));
                        }
                        break;
                }
            }
            return false;
        }).add(() -> {
            if (!words.get(0).equals("help")) {
                if (words.size() > 1) {
                    List args = (List) commands.get(words.get(0));
                    methods = (List) commandMethods.get(words.get(0));
                    index = getMethod(words, args.iterator(), 1);
                    if (index != -1) {
                        LOGGER.info("Terminal function is awaiting more input");
                        t.changeStatus(true);
                        t.getDispatcher().callEvent(new EnteringFunction(t, words.get(1)));
                        return true;
                    } else {
                        t.getDispatcher().callEvent(new InvalidArgument(t));
                    }
                }

            }
            return false;
        });
        if (words.size() > 0) {
            for (Boolean b : Objects.requireNonNull(Async.execute(genericList))) {
                if (b) {
                    return false;
                }
            }
            return Objects.requireNonNull(Async.execute(list)).get(2);
        }
        return false;
    }

    private int getMethod(final ArrayList<String> words, final Iterator args, final int compare) {
        int index = -1, count = 0;
        if (args != null) {
            while (args.hasNext()) {
                Object object = args.next();
                if (words.get(compare).equals(object.toString())) {
                    index = count;
                    break;
                }
                count++;
            }
        }
        return index;
    }


    List getMethods() {
        return methods;
    }

    ArrayList<String> getWords() {
        return words;
    }

    int getIndex() {
        return index;
    }

    /**
     * @param s input
     * @param m all methods from a generic file
     * @return if the input is a generic command
     */
    private boolean checkGeneric(final String s, final Method[] m) {
        for (Method M : m) {
            if (s.toLowerCase().equals(M.getName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param c generic class
     * @param s input
     * @return new instance from the generic class
     * @throws IllegalAccessException    n/a
     * @throws InstantiationException    n/a
     * @throws InvocationTargetException n/a
     */
    private Object executeGeneric(final Class<?> c, final String s) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object o = null;
        for (Method m : c.getDeclaredMethods()) {
            if (s.toLowerCase().equals(m.getName().toLowerCase())) {
                o = m.invoke(c.getConstructors()[0].newInstance(t));
                break;
            }
        }
        return o;
    }

    Class<?> getDefaultClass() {
        return defaultClass;
    }

    Class<?> getAdminClass() {
        return adminClass;
    }
}
