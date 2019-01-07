package ideaeclipse.DiscordAPI.bot;

import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.presence.UserStatus;
import ideaeclipse.DiscordAPI.bot.objects.presence.game.GameType;
import ideaeclipse.DiscordAPI.bot.objects.reaction.IReaction;
import ideaeclipse.DiscordAPI.bot.objects.role.IRole;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPI.utils.MultiKeyMap;
import ideaeclipse.reflectionListener.EventManager;

import java.util.Map;

/**
 * Allows for access to specific resources only for internal classes. Not user controled Ones
 *
 * @author Ideaeclipse
 * @see DiscordBot
 */
public interface IPrivateBot {
    EventManager getManager();

    MultiKeyMap<Long, String, IDiscordUser> getUsers();

    MultiKeyMap<Long, String, IChannel> getChannels();

    MultiKeyMap<Long, String, IRole> getRoles();

    Map<String, IReaction> getReactions();

    void setStatus(final GameType type, final String message, final UserStatus status);

    IChannel createDmChannel(final IDiscordUser user);
}
