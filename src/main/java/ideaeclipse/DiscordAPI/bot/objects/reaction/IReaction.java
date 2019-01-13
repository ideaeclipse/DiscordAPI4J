package ideaeclipse.DiscordAPI.bot.objects.reaction;

import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;

/**
 * Interface for {@link Reaction} to protect accessible data
 *
 * @author Ideaeclipse
 * @see AddReaction
 * @see RemoveReaction
 * @see Reaction
 */
public interface IReaction {
    /**
     * @return emoji code to type into channel surrounded with :'code':
     */
    String getCode();

    /**
     * @return actual emoji
     */
    String getEmoji();

    /**
     * @return channel where the reaction was sent
     */
    IChannel getChannel();

    /**
     * @return message object where the reaction was added
     */
    IMessage getMessage();
}
