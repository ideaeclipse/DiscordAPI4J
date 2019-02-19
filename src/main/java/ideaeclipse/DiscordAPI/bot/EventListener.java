package ideaeclipse.DiscordAPI.bot;

import ideaeclipse.DiscordAPI.bot.objects.channel.Field;
import ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.CreateChannel;
import ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.DeleteChannel;
import ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels.UpdateChannel;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPI.bot.objects.message.MessageCreate;
import ideaeclipse.DiscordAPI.bot.objects.presence.PresenceUpdate;
import ideaeclipse.DiscordAPI.bot.objects.reaction.AddReaction;
import ideaeclipse.DiscordAPI.bot.objects.reaction.RemoveReaction;
import ideaeclipse.DiscordAPI.bot.objects.role.CreateRole;
import ideaeclipse.DiscordAPI.bot.objects.role.DeleteRole;
import ideaeclipse.DiscordAPI.bot.objects.role.UpdateRole;
import ideaeclipse.DiscordAPI.bot.objects.user.CreateDiscordUser;
import ideaeclipse.DiscordAPI.bot.objects.user.DeleteDiscordUser;
import ideaeclipse.DiscordAPI.bot.objects.user.UpdateDiscordUser;
import ideaeclipse.customLogger.CustomLogger;
import ideaeclipse.customTerminal.CommandsClass;
import ideaeclipse.customTerminal.CustomTerminal;
import ideaeclipse.customTerminal.exceptions.ImproperCommandFormat;
import ideaeclipse.reflectionListener.annotations.CallableEvent;
import ideaeclipse.reflectionListener.parents.Event;
import ideaeclipse.reflectionListener.parents.Listener;

import java.util.List;

/**
 * This class is an extension of {@link Listener} and is used to output/log all possible events sent from the websocket
 * it is also used to handle message input for commands {@link #commandLogic(MessageCreate)}
 *
 * @author Ideaeclipse
 */
@SuppressWarnings("ALL")
class EventListener implements Listener {
    private final CustomLogger logger;
    private final DiscordBot bot;
    private final CustomTerminal<IMessage> input;
    private final CustomTerminal<IMessage> dmInput;
    private final String embedFooter;
    private final String commandChannel;

    /**
     * Starts logger, starts command handler
     *
     * @param bot bot instance
     */
    EventListener(final DiscordBot bot, final String commandPrefix, final CommandsClass commandsClass, final CommandsClass dmClass, final String embedFooter, final String commandChannel) {
        this.bot = bot;
        this.embedFooter = embedFooter;
        this.commandChannel = commandChannel;
        this.logger = new CustomLogger(this.getClass(), bot.getLoggerManager());
        if (commandsClass != null)
            this.input = new CustomTerminal<>(commandPrefix, commandsClass, IMessage.class);
        else
            this.input = null;
        if (dmClass != null)
            this.dmInput = new CustomTerminal<>(commandPrefix, dmClass, IMessage.class);
        else
            dmInput = null;
    }

    /**
     * Called when {@link MessageCreate} payload is sent
     * Handles all command input
     * {@link CustomTerminal#handleInput(String, Object)}
     *
     * @param create {@link Event} all methods with this event type are called when {@link ideaeclipse.reflectionListener.ListenerManager#callExecutables(Event)} is called with that event type
     */
    @CallableEvent
    private void commandLogic(final MessageCreate create) {
        IMessage message = create.getMessage();
        if (message.getChannel().getType() != 1) {
            this.logger.info("ChannelMessageCreate: " + message);
            if (this.input != null) {
                if (!message.getUser().getUsername().equals(this.bot.getBot().getUsername()) && (message.getChannel().equals(bot.getChannels().getByK2(this.commandChannel)) && message.getContent().startsWith(this.input.getPrefix()))) {
                    try {
                        String r = String.valueOf(this.input.handleInput(message.getContent(), message));
                        if (!r.equals("null")) {
                            List<Field> fieldList = Field.parser(r);
                            if (fieldList.size() > 0)
                                message.getChannel().sendEmbed(fieldList, this.embedFooter);
                            else
                                message.getChannel().sendMessage(r);
                        }
                    } catch (ImproperCommandFormat e) {
                        message.getChannel().sendMessage(e.getMessage());
                    }
                }
            }
        } else {
            this.logger.info("DmMessageCreate: " + message);
            if(this.input !=null && this.dmInput !=null) {
                if (!message.getUser().getUsername().equals(this.bot.getBot().getUsername())) {
                    if (message.getContent().startsWith(this.input.getPrefix())) {
                        try {
                            String r = String.valueOf(this.dmInput.handleInput(message.getContent(), message));
                            if (!r.equals("null")) {
                                List<Field> fieldList = Field.parser(r);
                                if (fieldList.size() > 0)
                                    message.getChannel().sendEmbed(fieldList, this.embedFooter);
                                else
                                    message.getChannel().sendMessage(r);
                            }
                        } catch (ImproperCommandFormat e) {
                            message.getChannel().sendMessage(e.getMessage());
                        }
                    } else
                        message.getChannel().sendMessage("All messages sent through dm's are meant to be commands use " + this.dmInput.getPrefix() + "help for commands");
                }
            }
        }
    }

    /**
     * Called when {@link PresenceUpdate} payload is sent
     * Prints the updated presence object
     *
     * @param update {@link Event} all methods with this event type are called when {@link ideaeclipse.reflectionListener.ListenerManager#callExecutables(Event)} is called with that event type
     */
    @CallableEvent
    private void onPresenceUpadte(final PresenceUpdate update) {
        this.logger.info("Presence Update: " + update.getPresence());
    }

    /**
     * Called when {@link CreateRole} payload is sent
     * Prints the role name that was created
     *
     * @param role {@link Event} all methods with this event type are called when {@link ideaeclipse.reflectionListener.ListenerManager#callExecutables(Event)} is called with that event type
     * @see ideaeclipse.DiscordAPI.webSocket.Wss
     */
    @CallableEvent
    private void onRoleCreate(final CreateRole role) {
        this.logger.info("Role added: " + role.getRole().getName());
    }

    /**
     * Called when {@link UpdateRole} payload is sent
     * Prints the role name that was updated
     *
     * @param role {@link Event} all methods with this event type are called when {@link ideaeclipse.reflectionListener.ListenerManager#callExecutables(Event)} is called with that event type
     * @see ideaeclipse.DiscordAPI.webSocket.Wss
     */
    @CallableEvent
    private void onRoleUpdate(final UpdateRole role) {
        this.logger.info("Role Updated: " + role.getRole().getName());
    }

    /**
     * Called when {@link DeleteRole} payload is sent
     * Prints the role name that was deleted
     *
     * @param role {@link Event} all methods with this event type are called when {@link ideaeclipse.reflectionListener.ListenerManager#callExecutables(Event)} is called with that event type
     * @see ideaeclipse.DiscordAPI.webSocket.Wss
     */
    @CallableEvent
    private void onDeleteRole(final DeleteRole role) {
        this.logger.info("Role delete: " + role.getRole().getName());
    }

    /**
     * Called when {@link CreateChannel} payload is sent
     * Prints the channel name that was created
     *
     * @param channel {@link Event} all methods with this event type are called when {@link ideaeclipse.reflectionListener.ListenerManager#callExecutables(Event)} is called with that event type
     * @see ideaeclipse.DiscordAPI.webSocket.Wss
     */
    @CallableEvent
    private void onChannelCreate(final CreateChannel channel) {
        this.logger.info("Channel added: " + channel.getChannel().getName());
    }

    /**
     * Called when {@link UpdateChannel} payload is sent
     * Prints the channel name that was updated
     *
     * @param channel {@link Event} all methods with this event type are called when {@link ideaeclipse.reflectionListener.ListenerManager#callExecutables(Event)} is called with that event type
     * @see ideaeclipse.DiscordAPI.webSocket.Wss
     */
    @CallableEvent
    private void onChannelUpdate(final UpdateChannel channel) {
        this.logger.info("Channel updated: " + channel.getChannel().getName());
    }

    /**
     * Called when {@link DeleteChannel} payload is sent
     * Prints the channel name that was deleted
     *
     * @param channel {@link Event} all methods with this event type are called when {@link ideaeclipse.reflectionListener.ListenerManager#callExecutables(Event)} is called with that event type
     * @see ideaeclipse.DiscordAPI.webSocket.Wss
     */
    @CallableEvent
    private void onDeleteChannel(final DeleteChannel channel) {
        this.logger.info("Channel deleted: " + channel.getChannel().getName());
    }

    /**
     * Called when {@link CreateDiscordUser} payload is sent
     * Prints the user name that was added
     *
     * @param user {@link Event} all methods with this event type are called when {@link ideaeclipse.reflectionListener.ListenerManager#callExecutables(Event)} is called with that event type
     * @see ideaeclipse.DiscordAPI.webSocket.Wss
     */
    @CallableEvent
    private void onUserJoin(final CreateDiscordUser user) {
        this.logger.info("User added: " + user.getUser().getUsername());
    }

    /**
     * Called when {@link UpdateDiscordUser} payload is sent
     * Prints the user name that was updated
     *
     * @param user {@link Event} all methods with this event type are called when {@link ideaeclipse.reflectionListener.ListenerManager#callExecutables(Event)} is called with that event type
     * @see ideaeclipse.DiscordAPI.webSocket.Wss
     */
    @CallableEvent
    private void onUserUpdate(final UpdateDiscordUser user) {
        this.logger.info("User updated: " + user.getUser().getUsername());
    }

    /**
     * Called when {@link DeleteDiscordUser} payload is sent
     * Prints the user name that was deleted
     *
     * @param user {@link Event} all methods with this event type are called when {@link ideaeclipse.reflectionListener.ListenerManager#callExecutables(Event)} is called with that event type
     * @see ideaeclipse.DiscordAPI.webSocket.Wss
     */
    @CallableEvent
    private void onDeleteUser(final DeleteDiscordUser user) {
        this.logger.info("User deleted: " + user.getUser().getUsername());
    }

    /**
     * Called when {@link AddReaction} payload is sent
     * Prints the reaction object that was added
     *
     * @param reaction {@link Event} all methods with this event type are called when {@link ideaeclipse.reflectionListener.ListenerManager#callExecutables(Event)} is called with that event type
     * @see ideaeclipse.DiscordAPI.webSocket.Wss
     */
    @CallableEvent
    private void onAddReaction(final AddReaction reaction) {
        this.logger.info("Reaction added: " + reaction.getReaction());
    }

    /**
     * Called when {@link RemoveReaction} payload is sent
     * Prints the reaction object that was removed
     *
     * @param reaction {@link Event} all methods with this event type are called when {@link ideaeclipse.reflectionListener.ListenerManager#callExecutables(Event)} is called with that event type
     * @see ideaeclipse.DiscordAPI.webSocket.Wss
     */
    @CallableEvent
    private void onRemoveReaction(final RemoveReaction reaction) {
        this.logger.info("Reaction removed: " + reaction.getReaction());
    }
}