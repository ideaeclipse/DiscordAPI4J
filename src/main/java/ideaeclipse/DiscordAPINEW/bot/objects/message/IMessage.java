package ideaeclipse.DiscordAPINEW.bot.objects.message;

import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;

/**
 * Interface for message objects
 * {@link MessageCreate} creates a {@link Message} object
 *
 * @author Ideaeclipse
 * @see MessageCreate
 * @see ideaeclipse.DiscordAPINEW.webSocket.Wss
 */
public interface IMessage {
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
}
