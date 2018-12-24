package ideaeclipse.DiscordAPINEW.bot.objects.reaction;

import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.message.IMessage;

public interface IReaction {
    String getCode();

    String getEmoji();

    IChannel getChannel();

    IMessage getMessage();
}
