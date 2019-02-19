package ideaeclipse.DiscordAPI.bot.objects.message;

import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;

import java.util.Map;

/**
 * Interface for message objects
 * {@link MessageCreate} creates a {@link Message} object
 *
 * @author Ideaeclipse
 * @see MessageCreate
 * @see Wss
 */
public interface IMessage {
    /**
     * @return message id
     */
    long getId();

    /**
     * @return message
     */
    String getContent();

    /**
     * @return channel the message was sent in
     * @see IChannel
     */
    IChannel getChannel();

    /**
     * @return user who sent the message
     * @see IDiscordUser
     */
    IDiscordUser getUser();

    /**
     * @return map of emoji code: number of values
     */
    Map<String, Integer> getReactions();

    /**
     * @param emoji if present increase value, else put with value 1
     */
    void addReaction(final String emoji);

    /**
     * @param emoji decreases emoji value by 1
     */
    void removeReaction(final String emoji);
}
