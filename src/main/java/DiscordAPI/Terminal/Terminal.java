package DiscordAPI.Terminal;

import DiscordAPI.IDiscordBot;
import DiscordAPI.Terminal.Input.ParseInput;
import DiscordAPI.Terminal.logic.Compare;
import DiscordAPI.Terminal.logic.Execute;
import DiscordAPI.listener.genericListener.IDispatcher;
import DiscordAPI.listener.terminalListener.listenerTypes.Commands.ProgramReturnValues;
import DiscordAPI.objects.Interfaces.IDiscordUser;
import DiscordAPI.objects.Interfaces.IMessage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Terminal {
    private static Logger LOGGER = Logger.getLogger(Terminal.class.getName());
    private final IDiscordBot bot;
    private String additionalInput;
    private boolean status;
    private Compare compare;
    private IDiscordUser user;
    private Execute e;
    private IDispatcher dispatcher;
    private IMessage m;
    private boolean isAdmin = false;
    private String currentFunction;

    public Terminal(final IDiscordUser u, final IDiscordBot bot) {
        this.bot = bot;
        user = u;
        dispatcher = new IDispatcher();
        if (u.getName().toLowerCase().equals(bot.getProperties().getProperty("adminUser"))) {
            isAdmin = true;
        }
    }

    public IDiscordBot getBot() {
        return this.bot;
    }

    public boolean initate(String command) throws
            ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException {
        compare = new Compare();
        status = false;
        ParseInput pi = new ParseInput(command);
        return compare.Initiate(pi.getWords(new ArrayList<>()), this);
    }

    public void changeStatus(boolean b) {
        status = b;
    }

    public boolean requiresMoreInput() {
        return status;
    }

    public void addMoreInput(String s) {
        additionalInput = s;
        List methods = compare.getMethods();
        int index = compare.getIndex();
        ArrayList<String> words = compare.getWords();
        if (e == null) {
            try {
                e = new Execute(methods.get(index).toString(), words, this);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e1) {
                e1.printStackTrace();
            } catch (NoSuchMethodException e) {
                Object o = checkDefault(this.getAdditionalInput());
                if (o != null) {
                    this.getDispatcher().notify(new ProgramReturnValues(this, (String) o));
                }
            }
        } else {
            try {
                e.method(e.getObject(), e.getCalledClass());
            } catch (InvocationTargetException | IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (NoSuchMethodException e) {
                Object o = checkDefault(this.getAdditionalInput());
                if (o != null) {
                    this.getDispatcher().notify(new ProgramReturnValues(this, (String) o));
                }
            }
        }
    }

    private Object checkDefault(ArrayList<String> words) {
        LOGGER.info("CHECK default");
        Execute m = new Execute(this);
        Object o = null;
        try {
            o = m.invoke(compare.getDefaultClass(), words);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e1) {
            e1.printStackTrace();
        }
        return o;
    }

    public ArrayList<String> getAdditionalInput() {
        ParseInput pi = new ParseInput(this.additionalInput);
        return pi.getWords(new ArrayList<>());
    }

    public IDiscordUser getUser() {
        return user;
    }

    public IDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void setMessage(IMessage m) {
        this.m = m;
    }

    public IMessage getMessage() {
        return this.m;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setCurrentFunction(String function) {
        this.currentFunction = function;
    }

    public String getCurrentFunction() {
        return this.currentFunction;
    }
}
