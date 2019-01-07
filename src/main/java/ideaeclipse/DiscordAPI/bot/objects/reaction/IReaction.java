package ideaeclipse.DiscordAPI.bot.objects.reaction;

import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;

public interface IReaction {
    String getCode();

    String getEmoji();

    IChannel getChannel();

    IMessage getMessage();
}
