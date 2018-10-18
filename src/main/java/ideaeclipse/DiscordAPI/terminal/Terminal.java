package ideaeclipse.DiscordAPI.terminal;

import ideaeclipse.AsyncUtility.Async;
import ideaeclipse.DiscordAPI.IDiscordBot;
import ideaeclipse.DiscordAPI.objects.IDiscordUser;
import ideaeclipse.DiscordAPI.objects.Interfaces.IRole;
import ideaeclipse.DiscordAPI.utils.DiscordUtils;
import ideaeclipse.reflectionListener.EventManager;
import ideaeclipse.reflectionListener.Listener;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Terminal {
    private final IDiscordBot bot;
    private final EventManager dispatcher;
    private final IDiscordUser user;
    private final CommandList commandList;
    private InputHandler inputHandler;
    private boolean status;
    private boolean isAdmin = false;
    private String currentFunction;

    public Terminal(final IDiscordUser u, final IDiscordBot bot, final Listener eventListener, final CommandList commandMap) {
        this.bot = bot;
        this.user = u;
        this.commandList = commandMap;
        this.dispatcher = new EventManager();
        this.dispatcher.registerListener(eventListener);
        if (user.getName().toLowerCase().equals(bot.getProperties().getProperty("adminUser"))) {
            this.isAdmin = true;
        } else {
            Async.queue(x -> {
                String adminGroup = bot.getProperties().getProperty("adminGroup");
                for (IRole role : IDiscordUser.getServerUniqueUser(user).getRoles()) {
                    if (role.equals(DiscordUtils.Search.ROLES(bot.getRoles(), adminGroup))) {
                        isAdmin = true;
                        break;
                    }
                }
                return Optional.empty();
            }, "adminGroup");
        }
    }

    public boolean initiate(String command) {
        this.inputHandler = new InputHandler(new ParseInput(command).getWords(new ArrayList<>()), this);
        return this.inputHandler.start();
    }

    public void addMoreInput(String s) {
        System.out.println("MORE INPUT:" + s);
        if (this.inputHandler != null) {
            this.inputHandler.getFunction().executeMethod(new ParseInput(s).getWords(new ArrayList<>()));
        }
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

    void changeStatus(boolean newStatus) {
        status = newStatus;
    }

    public boolean requiresMoreInput() {
        return status;
    }

    public IDiscordBot getBot() {
        return this.bot;
    }

    public CommandList getCommandList() {
        return commandList;
    }
}
