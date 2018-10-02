package ideaeclipse.DiscordAPI.Terminal;

import ideaeclipse.DiscordAPI.IDiscordBot;
import ideaeclipse.DiscordAPI.objects.IDiscordUser;
import ideaeclipse.DiscordAPI.objects.Interfaces.IRole;
import ideaeclipse.DiscordAPI.utils.DiscordUtils;
import ideaeclipse.AsyncUtility.Async;
import ideaeclipse.reflectionListener.EventManager;
import ideaeclipse.reflectionListener.Listener;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Terminal {
    private final IDiscordBot bot;
    private String additionalInput;
    private boolean status;
    private Compare compare;
    private IDiscordUser user;
    private Execute execute;
    private EventManager dispatcher;
    private boolean isAdmin = false;
    private String currentFunction;

    public Terminal(final IDiscordUser u, final IDiscordBot bot, final Listener eventListener) {
        this.bot = bot;
        user = u;
        dispatcher = new EventManager();
        dispatcher.registerListener(eventListener);
        if (user.getName().toLowerCase().equals(bot.getProperties().getProperty("adminUser"))) {
            isAdmin = true;
        } else {
            Async.queue(() -> {
                String adminGroup = bot.getProperties().getProperty("adminGroup");
                for (IRole role : IDiscordUser.getServerUniqueUser(user).getRoles()) {
                    if (role.equals(DiscordUtils.Search.ROLES(bot.getRoles(), adminGroup))) {
                        isAdmin = true;
                        break;
                    }
                }
                return null;
            }, "adminGroup");
        }
    }

    public IDiscordBot getBot() {
        return this.bot;
    }

    public boolean initiate(String command) throws
            ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException {
        compare = new Compare();
        status = false;
        ParseInput pi = new ParseInput(command);
        return compare.Initiate(pi.getWords(new ArrayList<>()), this);
    }

    void changeStatus(boolean newStatus) {
        status = newStatus;
    }

    public boolean requiresMoreInput() {
        return status;
    }

    public void addMoreInput(String s) {
        additionalInput = s;
        List methods = compare.getMethods();
        int index = compare.getIndex();
        ArrayList<String> words = compare.getWords();
        if (execute == null) {
            try {
                execute = new Execute(methods.get(index).toString(), words, this, compare.getDefaultClass(), compare.getAdminClass());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        } else {
            execute.method(execute.getObject(), execute.getCalledClass(), compare.getDefaultClass(), compare.getAdminClass());
        }
    }

    ArrayList<String> getAdditionalInput() {
        ParseInput pi = new ParseInput(this.additionalInput);
        return pi.getWords(new ArrayList<>());
    }

    public IDiscordUser getUser() {
        return user;
    }

    public EventManager getDispatcher() {
        return this.dispatcher;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setCurrentFunction(final String function) {
        this.currentFunction = function;
    }

    public String getCurrentFunction() {
        return this.currentFunction;
    }
}
