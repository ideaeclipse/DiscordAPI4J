package DiscordAPI.objects;

import DiscordAPI.IDiscordBot;
import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.discordApiListener.ApiEvent;
import DiscordAPI.listener.discordApiListener.listenerEvents.Message_Create;
import DiscordAPI.listener.terminalListener.listenerTypes.Commands.*;
import DiscordAPI.listener.genericListener.IListener;
import DiscordAPI.listener.terminalListener.listenerTypes.TerminalEvent;
import DiscordAPI.listener.terminalListener.listenerTypes.errors.*;
import DiscordAPI.listener.terminalListener.listenerTypes.terminal.NeedsMoreInput;
import DiscordAPI.objects.Interfaces.IChannel;
import DiscordAPI.objects.Interfaces.IMessage;
import DiscordAPI.utils.Async;
import DiscordAPI.utils.DiscordLogger;
import DiscordAPI.utils.DiscordUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This class is online turned on if the config option useTerminal is set to true
 * This class manages all things related to the custom terminal
 *
 * @author Myles
 * @see TerminalEvent
 * @see DiscordAPI.listener.genericListener.IDispatcher
 */
class TerminalManager {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final IDiscordBot bot;
    private final List<Terminal> terminalList;

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

    /**
     * Main logic for terminal control
     */
    private void terminalLogic() {
        bot.getDispatcher().addListener((IListener<ApiEvent, Message_Create>) messageReceivedEvent -> {
            IMessage m = messageReceivedEvent.getMessage();
            Terminal terminal = getCurrentTerminal(m.getUser());
            if (m.getChannel().getId().equals(Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).getId())) {
                Async.AsyncList<?> list = new Async.AsyncList<>();
                list.add(() -> {
                    if (terminal != null) {
                        if (terminal.requiresMoreInput()) {
                            terminal.getDispatcher().notify(new NeedsMoreInput(terminal, m));
                        }
                    }
                    return null;
                }).add(() -> {
                    if (terminal == null) {
                        if (m.getContent().startsWith("cm")) {
                            if (m.getContent().equals("cm help")) {
                                invoke(addListeners(new Terminal(m.getUser(), bot)), m);
                            } else {
                                terminalList.add(addListeners(new Terminal(m.getUser(), bot)));
                                if (!invoke(terminalList.get(terminalList.size() - 1), m)) {
                                    terminalList.remove(terminalList.size() - 1);
                                }

                            }
                        }
                    }
                    return null;
                });
                Async.execute(list);
            } else {
                if (m.getContent().equals("cm") || m.getContent().startsWith("cm"))
                    m.getChannel().sendMessage("Use the bot channel to execute bot related commands");
            }
        });
    }

    /**
     * Adds all listener events to each terminal
     *
     * @param terminal passes terminal to add listeners
     * @return terminal with all listeners added
     */
    private Terminal addListeners(final Terminal terminal) {
        final IChannel botChannel = Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot"));
        //terminal
        terminal.getDispatcher().addListener((IListener<TerminalEvent, NeedsMoreInput>) a -> {
            Terminal t = a.getTerminal();
            IMessage me = a.getMessage();
            t.addMoreInput(me.getContent());
        });
        //commands
        terminal.getDispatcher().addListener((IListener<TerminalEvent, BotCommands>) a -> botChannel.sendMessage(a.getReturn()));
        terminal.getDispatcher().addListener((IListener<TerminalEvent, ClassInfo>) a -> botChannel.sendMessage(a.getReturn()));
        terminal.getDispatcher().addListener((IListener<TerminalEvent, EnteringFunction>) a -> botChannel.sendMessage(a.getReturn()));
        terminal.getDispatcher().addListener((IListener<TerminalEvent, ExitingFunction>) a -> {
            botChannel.sendMessage(a.getReturn());
            terminalList.remove(a.getTerminal());
        });
        terminal.getDispatcher().addListener((IListener<TerminalEvent, ProgramReturnValues>) a -> {
            if (a.getReturn().length() > 0) {
                botChannel.sendMessage(a.getReturn());
            }
        });
        //errors
        terminal.getDispatcher().addListener((IListener<TerminalEvent, InvalidArgument>) a -> botChannel.sendMessage(a.getReturn()));
        terminal.getDispatcher().addListener((IListener<TerminalEvent, InvalidCommand>) a -> botChannel.sendMessage(a.getReturn()));
        terminal.getDispatcher().addListener((IListener<TerminalEvent, InvalidHelpFormat>) a -> botChannel.sendMessage(a.getReturn()));
        terminal.getDispatcher().addListener((IListener<TerminalEvent, MissingParameters>) a -> botChannel.sendMessage(a.getReturn()));
        terminal.getDispatcher().addListener((IListener<TerminalEvent, NoSuchMethod>) a -> botChannel.sendMessage(a.getReturn()));
        terminal.getDispatcher().addListener((IListener<TerminalEvent, WrongNumberOfArgs>) a -> botChannel.sendMessage(a.getReturn()));
        terminal.getDispatcher().addListener((IListener<TerminalEvent, WrongType>) a -> botChannel.sendMessage(a.getReturn()));
        return terminal;
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
}
