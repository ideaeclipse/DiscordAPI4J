package ideaeclipse.DiscordAPI.bot;

import ideaeclipse.CustomProperties.Properties;
import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.presence.UserStatus;
import ideaeclipse.DiscordAPI.bot.objects.presence.game.GameType;
import ideaeclipse.DiscordAPI.bot.objects.role.IRole;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPI.utils.MultiKeyMap;
import ideaeclipse.DiscordAPI.utils.interfaces.IHttpRequests;
import ideaeclipse.DiscordAPI.webSocket.rateLimit.RateLimitRecorder;
import ideaeclipse.customLogger.LoggerManager;
import ideaeclipse.reflectionListener.EventManager;

/**
 * Allows for encapsulation of {@link DiscordBot} object
 *
 * @author Ideaeclipse
 * @see DiscordBot
 */
public interface IDiscordBot {
    /**
     * @return {@link DiscordBot} object of the bot
     */
    IDiscordUser getBot();

    /**
     * @return bot's guild it
     */
    Long getGuildId();

    /**
     * @return map of users
     * @see MultiKeyMap
     */
    MultiKeyMap<Long, String, IDiscordUser> getUsers();

    /**
     * @return map of channels
     * @see MultiKeyMap
     */
    MultiKeyMap<Long, String, IChannel> getChannels();

    /**
     * @return map of roles
     * @see MultiKeyMap
     */
    MultiKeyMap<Long, String, IRole> getRoles();

    /**
     * Sets bots rich presence status in discord
     * Doesn't work unless status is online
     *
     * @param type    Listening, playing, streaming
     * @param message message to display
     * @param status  online, idle, dnd, invisible, offline
     */
    void setStatus(final GameType type, final String message, final UserStatus status);

    /**
     * Creates a dm channel between you and a user
     *
     * @param user user {@link IDiscordBot#getUsers()}
     * @return channel object
     */
    IChannel createDmChannel(final IDiscordUser user);

    /**
     * @return returns eventManager
     */
    EventManager getEventManager();

    /**
     * @return returns loggerManager
     */
    LoggerManager getLoggerManager();

    /**
     * @return returns properties manager
     */
    Properties getProperties();

    /**
     * @return whether channel messages get queries
     */
    boolean queryMessages();

    /**
     * @return rate limit recorder
     */
    RateLimitRecorder getRateLimitRecorder();

    /**
     * @return requests
     */
    IHttpRequests getRequests();
}
