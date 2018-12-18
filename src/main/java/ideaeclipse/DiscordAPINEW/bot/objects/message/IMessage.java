package ideaeclipse.DiscordAPINEW.bot.objects.message;

import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;

public interface IMessage {
    String getContent();

    IChannel getChannel();

    IDiscordUser getUser();
}
