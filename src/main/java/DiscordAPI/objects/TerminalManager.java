package DiscordAPI.objects;

import DiscordAPI.IDiscordBot;
import DiscordAPI.Terminal.Terminal;
import DiscordAPI.listener.discordApiListener.IListener;
import DiscordAPI.listener.discordApiListener.listenerEvents.Message_Create;
import DiscordAPI.listener.terminalListener.listenerTypes.Commands.*;
import DiscordAPI.listener.terminalListener.listenerTypes.TListener;
import DiscordAPI.listener.terminalListener.listenerTypes.errors.*;
import DiscordAPI.listener.terminalListener.listenerTypes.terminal.NeedsMoreInput;
import DiscordAPI.objects.Interfaces.IDiscordUser;
import DiscordAPI.objects.Interfaces.IMessage;
import DiscordAPI.utils.DiscordLogger;
import DiscordAPI.utils.DiscordUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TerminalManager {
    private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
    private final IDiscordBot bot;
    private final List<Terminal> terminalList;

    public TerminalManager(final IDiscordBot bot) {
        logger.info("Starting Terminal Manager");
        this.bot = bot;
        terminalList = new LinkedList<>();
        terminalLogic();
    }

    private Terminal getCurrentTerminal(IDiscordUser user) {
        for (Terminal t : terminalList) {
            if (t.getUser().equals(user)) {
                return t;
            }
        }
        return null;
    }

    private void terminalLogic() {
        bot.getDispatcher().addListener((IListener<Message_Create>) messageReceivedEvent -> {
            IMessage m = messageReceivedEvent.getMessage();
            Terminal terminal = getCurrentTerminal(m.getUser());
            if (m.getChannel().getId().equals(Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).getId())) {
                logger.info("TERMINAL SIZE: " + terminalList.size());
                if (terminal != null) {
                    if (terminal.requiresMoreInput() && terminal.getUser().equals(m.getUser())) {
                        terminal.setMessage(m);
                        terminal.getDispatcher().notify(new NeedsMoreInput(terminal, m));
                    } else {
                        if (!m.getContent().startsWith("cm"))
                            m.getChannel().sendMessage("You currently aren't attached to a terminal session please type cm $ParentArg $SubArg to start a session");
                    }
                }
                if (m.getContent().equals("cm") || m.getContent().startsWith("cm")) {
                    if (terminalList.size() > 0) {
                        for (Terminal t : terminalList) {
                            if (t.getUser() == m.getUser()) {
                                if (Objects.requireNonNull(terminal).requiresMoreInput()) {
                                    m.getChannel().sendMessage("Commands that start with cm are for when you're outside a function");
                                } else {
                                    terminalList.remove(t);
                                    terminalList.add(addListeners(new Terminal(m.getUser(), bot)));
                                    invoke(terminalList.get(terminalList.size() - 1), m);
                                }
                            } else {
                                terminalList.add(addListeners(new Terminal(m.getUser(), bot)));
                                invoke(terminalList.get(terminalList.size() - 1), m);
                            }
                        }
                    } else {
                        if (!m.getContent().contains("help")) {
                            terminalList.add(addListeners(new Terminal(m.getUser(), bot)));
                            if (!invoke(terminalList.get(terminalList.size() - 1), m)) {
                                terminalList.remove(terminalList.size() - 1);
                            }
                        } else {
                            invoke(addListeners(new Terminal(m.getUser(), bot)), m);
                        }
                    }
                }
            } else {
                if (m.getContent().equals("cm") || m.getContent().startsWith("cm"))
                    m.getChannel().sendMessage("Use the bot channel to execute bot related commands");
            }
        });
    }


    private Terminal addListeners(Terminal terminal) {
        //terminal
        terminal.getDispatcher().addListener((TListener<NeedsMoreInput>) a -> {
            Terminal t = a.getTerminal();
            IMessage me = a.getMessage();
            t.addMoreInput(me.getContent());
        });
        //commands
        terminal.getDispatcher().addListener((TListener<BotCommands>) a -> {
            Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).sendMessage(a.getReturn());
        });
        terminal.getDispatcher().addListener((TListener<ClassInfo>) a -> {
            Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).sendMessage(a.getReturn());
        });
        terminal.getDispatcher().addListener((TListener<EnteringFunction>) a -> {
            Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).sendMessage(a.getReturn());
        });
        terminal.getDispatcher().addListener((TListener<ExitingFunction>) a -> {
            Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).sendMessage(a.getReturn());
            terminalList.remove(a.getTerminal());
        });
        terminal.getDispatcher().addListener((TListener<ProgramReturnValues>) a -> {
            if (a.getReturn().length() > 0) {
                Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).sendMessage(a.getReturn());
            }
        });
        //errors
        terminal.getDispatcher().addListener((TListener<InvalidArgument>) a -> {
            Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).sendMessage(a.getReturn());
        });
        terminal.getDispatcher().addListener((TListener<InvalidCommand>) a -> {
            Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).sendMessage(a.getReturn());
        });
        terminal.getDispatcher().addListener((TListener<InvalidHelpFormat>) a -> {
            Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).sendMessage(a.getReturn());
        });
        terminal.getDispatcher().addListener((TListener<MissingParameters>) a -> {
            Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).sendMessage(a.getReturn());
        });
        terminal.getDispatcher().addListener((TListener<NoSuchMethod>) a -> {
            Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).sendMessage(a.getReturn());
        });
        terminal.getDispatcher().addListener((TListener<WrongNumberOfArgs>) a -> {
            Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).sendMessage(a.getReturn());
        });
        terminal.getDispatcher().addListener((TListener<WrongType>) a -> {
            Objects.requireNonNull(DiscordUtils.Search.CHANNEL(bot.getChannels(), "bot")).sendMessage(a.getReturn());
        });
        return terminal;
    }

    private boolean invoke(Terminal t, IMessage m) {
        boolean status = false;
        try {
            status = t.initate(m.getContent().substring(3, m.getContent().length()));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return status;
    }
}
