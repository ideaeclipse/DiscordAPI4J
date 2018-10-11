package ideaeclipse.DiscordAPI.objects;

import ideaeclipse.AsyncUtility.AsyncList;
import ideaeclipse.AsyncUtility.ForEachList;
import ideaeclipse.DiscordAPI.IDiscordBot;
import ideaeclipse.DiscordAPI.Terminal.Terminal;
import ideaeclipse.DiscordAPI.listener.discordApiListener.listenerEvents.Message_Create;
import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.Commands.*;
import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.errors.*;
import ideaeclipse.DiscordAPI.listener.terminalListener.listenerTypes.terminal.NeedsMoreInput;
import ideaeclipse.DiscordAPI.objects.Interfaces.IChannel;
import ideaeclipse.DiscordAPI.objects.Interfaces.IMessage;
import ideaeclipse.DiscordAPI.utils.DiscordLogger;
import ideaeclipse.DiscordAPI.utils.DiscordUtils;
import ideaeclipse.reflectionListener.EventHandler;
import ideaeclipse.reflectionListener.Listener;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This class is online turned on if the config option useTerminal is set to true
 * This class manages all things related to the custom terminal
 *
 * @author ideaeclipse
 * @see TerminalEvent
 */
class TerminalManager {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final IDiscordBot bot;
    private final List<Terminal> terminalList;
    final IChannel botChannel;

    /**
     * @param bot passes the bot to get properties
     */
    TerminalManager(final IDiscordBot bot) {
        logger.info("Starting Terminal Manager");
        if (bot.getProperties().getProperty("debug").equals("true")) {
            logger.setLevel(DiscordLogger.Level.TRACE);
        }
        this.bot = bot;
        terminalList = new LinkedList<>();
        this.botChannel = Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot"));
        terminalLogic();
    }

    /**
     * Method searches for a terminal linked to a past user
     *
     * @param user user passed
     * @return linked terminal to user if there is one
     */
    private Terminal getCurrentTerminal(final IDiscordUser user) {
        for (Terminal t : terminalList) {
            if (t.getUser().equals(user)) {
                return t;
            }
        }
        return null;
    }

    private void terminalLogic() {
        bot.getDispatcher().registerListener(new terminalMessageListener());
    }

    /**
     * @param t Terminal
     * @param m M
     * @return if true it will require more input if false delete the terminal
     */
    private boolean invoke(final Terminal t, final IMessage m) {
        boolean status = false;
        try {
            status = t.initiate(m.getContent().substring(3, m.getContent().length()));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return status;
    }

    public class terminalMessageListener implements Listener {
        @EventHandler
        public void onMessageEvent(Message_Create create) {
            IMessage m = create.getMessage();
            Terminal terminal = getCurrentTerminal(m.getUser());
            if (m.getChannel().getId().equals(Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).getId())) {
                AsyncList<?> list = new ForEachList<>();
                list.add(x -> {
                    if (terminal != null) {
                        if (terminal.requiresMoreInput()) {
                            terminal.getDispatcher().callEvent(new NeedsMoreInput(terminal, m));
                        }
                    }
                    return Optional.empty();
                }).add(x -> {
                    if (terminal == null) {
                        if (m.getContent().startsWith("cm")) {
                            if (m.getContent().equals("cm help")) {
                                invoke(new Terminal(m.getUser(), bot, new terminalListener()), m);
                            } else {
                                terminalList.add(new Terminal(m.getUser(), bot, new terminalListener()));
                                if (!invoke(terminalList.get(terminalList.size() - 1), m)) {
                                    terminalList.remove(terminalList.size() - 1);
                                }

                            }
                        }
                    }
                    return Optional.empty();
                });
                list.execute();
            } else {
                if (m.getContent().equals("cm") || m.getContent().startsWith("cm"))
                    m.getChannel().sendMessage("Use the bot channel to execute bot related commands");
            }
        }

    }

    public class terminalListener implements Listener {
        @EventHandler
        public void needsMoreInput(NeedsMoreInput input) {
            Terminal t = input.getTerminal();
            IMessage me = input.getMessage();
            t.addMoreInput(me.getContent());
        }

        @EventHandler
        public void onBotCommands(BotCommands commands) {
            botChannel.sendMessage(commands.getReturn());
        }

        @EventHandler
        public void onClassInfo(ClassInfo info) {
            botChannel.sendMessage(info.getReturn());
        }

        @EventHandler
        public void onEnteringFunction(EnteringFunction function) {
            botChannel.sendMessage(function.getReturn());
        }

        @EventHandler
        public void onExitingFunction(ExitingFunction function) {
            botChannel.sendMessage(function.getReturn());
            terminalList.remove(function.getTerminal());
        }

        @EventHandler
        public void onProgramReturnValues(ProgramReturnValues values) {
            if (values.getReturn().length() > 0) {
                botChannel.sendMessage(values.getReturn());
            }
        }

        @EventHandler
        public void onInvalidArgument(InvalidArgument argument) {
            botChannel.sendMessage(argument.getReturn());
        }

        @EventHandler
        public void onInvalidCommand(InvalidCommand argument) {
            botChannel.sendMessage(argument.getReturn());
        }

        @EventHandler
        public void onInvalidHelpFormat(InvalidHelpFormat argument) {
            botChannel.sendMessage(argument.getReturn());
        }

        @EventHandler
        public void onNoSuchMethod(NoSuchMethod argument) {
            botChannel.sendMessage(argument.getReturn());
        }

        @EventHandler
        public void onWrongNumberOfArgs(WrongNumberOfArgs argument) {
            botChannel.sendMessage(argument.getReturn());
        }

        @EventHandler
        public void onWrongType(WrongType argument) {
            botChannel.sendMessage(argument.getReturn());
        }

    }
}
