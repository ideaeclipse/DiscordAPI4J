package ideaeclipse.DiscordAPI.objects;

import ideaeclipse.DiscordAPI.objects.Interfaces.IChannel;
import ideaeclipse.DiscordAPI.objects.Interfaces.IMessage;

/**
 * Class is used to store message info
 *
 * @author Ideaeclipse
 * @see ideaeclipse.DiscordAPI.objects.Payloads.DMessage
 */
class Message implements IMessage {
    private final IDiscordUser user;
    private final IChannel channel;
    private final Long guildId;
    private final String content;

    /**
     * @param user    DiscordUser who sent the message
     * @param channel Channel in which the message was sent
     * @param guildId guildId
     * @param content contents of the message
     */
    Message(final IDiscordUser user, final IChannel channel, final Long guildId, final String content) {
        this.user = user;
        this.channel = channel;
        this.guildId = guildId;
        this.content = content;
    }

    public IChannel getChannel() {
        return channel;
    }

    public IDiscordUser getUser() {
        return user;
    }

    public Long getGuildId() {
        return guildId;
    }

    public String getContent() {
        return content;
    }
}