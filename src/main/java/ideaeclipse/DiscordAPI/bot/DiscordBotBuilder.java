package ideaeclipse.DiscordAPI.bot;

import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.interfaces.IHttpRequests;
import ideaeclipse.customLogger.CustomLogger;
import ideaeclipse.customLogger.Level;
import ideaeclipse.customLogger.LoggerManager;
import ideaeclipse.customTerminal.CommandsClass;
import ideaeclipse.reflectionListener.ListenerManager;
import ideaeclipse.reflectionListener.parents.Listener;

/**
 * Allows user to set what they want to add to the bot
 *
 * @author Ideaeclipse
 */
public class DiscordBotBuilder {
    private final String token;
    private final LoggerManager loggerManager;
    private final ListenerManager listenerManager;
    private final IHttpRequests requests;
    private final CustomLogger logger;
    private String commandPrefix;
    private CommandsClass commandClass;
    private CommandsClass dmClass;
    private String commandChannel;
    private String embedFooter;


    /**
     * Starts loggerManager, listenerManager, Requests
     *
     * @param token pass token to start the bot
     */
    public DiscordBotBuilder(final String token) {
        this.token = token;
        this.loggerManager = new LoggerManager("customLogger", Level.DEBUG);
        this.listenerManager = new ListenerManager();
        this.requests = new Util.HttpRequests(token);
        this.logger = new CustomLogger(this.getClass(), this.loggerManager);
    }

    /**
     * If you want to add custom event listeners use this method
     * @param listeners list of listeners, or individual listener
     * @return instance
     */
    public DiscordBotBuilder registerListener(final Listener... listeners) {
        for (Listener listener : listeners) {
            logger.debug("Registering user defined event with name: " + listener.getClass());
            this.listenerManager.registerListener(listener);
        }
        return this;
    }

    /**
     * Sets the command prefix for commands. i.e ! means you could do !help for help
     * @param commandPrefix command prefix
     * @return instance
     */
    public DiscordBotBuilder setCommandPrefix(final String commandPrefix) {
        this.commandPrefix = commandPrefix;
        return this;
    }

    /**
     * Sets the channelCommands class where the commands will be parsed
     * @param commandsClass commands class
     * @return instance
     */
    public DiscordBotBuilder channelCommands(final CommandsClass commandsClass) {
        logger.debug("Adding Channel Commands");
        this.commandClass = commandsClass;
        return this;
    }
    /**
     * Sets the dmCommands class where the commands will be parsed
     * @param commandsClass dm class
     * @return instance
     */
    public DiscordBotBuilder dmCommands(final CommandsClass commandsClass) {
        logger.debug("Adding DM Commands");
        this.dmClass = commandsClass;
        return this;
    }

    /**
     * Where commands are allowed to be executed
     * @param commandChannel commands channel
     * @return instance
     */
    public DiscordBotBuilder setCommandChannel(final String commandChannel){
        this.commandChannel = commandChannel;
        return this;
    }

    /**
     * Image to display at bottom of footer
     * @param embedFooter link
     * @return instance
     */
    public DiscordBotBuilder setEmbedFooter(final String embedFooter) {
        this.embedFooter = embedFooter;
        return this;
    }

    /**
     * This starts the bot
     * @return bot instance
     */
    public IDiscordBot start(){
        if(this.commandPrefix == null)
            this.commandPrefix = "!";
        return new DiscordBot(this.token,this.loggerManager,this.listenerManager,this.requests,this.commandPrefix,this.commandClass,this.dmClass,this.commandChannel,this.embedFooter);
    }
}
