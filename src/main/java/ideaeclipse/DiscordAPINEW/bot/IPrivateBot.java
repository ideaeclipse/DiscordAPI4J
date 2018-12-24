package ideaeclipse.DiscordAPINEW.bot;

import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.presence.UserStatus;
import ideaeclipse.DiscordAPINEW.bot.objects.presence.game.GameType;
import ideaeclipse.DiscordAPINEW.bot.objects.reaction.IReaction;
import ideaeclipse.DiscordAPINEW.bot.objects.role.IRole;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
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

    Map<Long, IDiscordUser> getUsers();

    Map<Long, IChannel> getChannels();

    Map<Long, IRole> getRoles();

    Map<String, IReaction> getReactions();

    void setStatus(final GameType type, final String message, final UserStatus status);

    IChannel createDmChannel(final IDiscordUser user);
}
