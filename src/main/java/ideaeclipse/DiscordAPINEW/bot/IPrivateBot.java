package ideaeclipse.DiscordAPINEW.bot;

import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
import ideaeclipse.reflectionListener.EventManager;

import java.util.Map;

public interface IPrivateBot {
    EventManager getManager();

    Map<Long, IDiscordUser> getUsers();

    Map<Long, IChannel> getChannels();
}
