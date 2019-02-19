package ideaeclipse.DiscordAPI.bot;

import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.presence.UserStatus;
import ideaeclipse.DiscordAPI.bot.objects.presence.game.GameType;
import ideaeclipse.DiscordAPI.bot.objects.role.IRole;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPI.utils.MultiKeyMap;

/**
 * Allows for encapsulation of {@link DiscordBot} object
 *
 * @author Ideaeclipse
 * @see DiscordBot
 */
public abstract class IDiscordBot {

    /**
     * @return {@link DiscordBot} object of the bot
     */
    public abstract IDiscordUser getBot();

    /**
     * @return bot's guild it
     */
    public abstract Long getGuildId();

    /**
     * @return map of users
     * @see MultiKeyMap
     */
    public abstract MultiKeyMap<Long, String, IDiscordUser> getUsers();

    /**
     * @return map of channels
     * @see MultiKeyMap
     */
    public abstract MultiKeyMap<Long, String, IChannel> getChannels();

    /**
     * @return map of roles
     * @see MultiKeyMap
     */
    public abstract MultiKeyMap<Long, String, IRole> getRoles();

    /**
     * Sets bots rich presence status in discord
     * Doesn't work unless status is online
     *
     * @param type    Listening, playing, streaming
     * @param message message to display
     * @param status  online, idle, dnd, invisible, offline
     */
    public abstract void setStatus(final GameType type, final String message, final UserStatus status);

    /**
     * Creates a dm channel between you and a user
     *
     * @param user user {@link IDiscordBot#getUsers()}
     * @return channel object
     */
    public abstract IChannel createDmChannel(final IDiscordUser user);
}
