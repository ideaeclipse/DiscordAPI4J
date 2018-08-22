package DiscordAPI.objects;

/**
 * Class is used to store message info
 *
 * @author Ideaeclipse
 * @see DiscordAPI.objects.Payloads.DMessage
 */
public class Message {
    private final DiscordUser user;
    private final Channel channel;
    private final Long guildId;
    private final String content;

    /**
     * @param user    DiscordUser who sent the message
     * @param channel Channel in which the message was sent
     * @param guildId guildId
     * @param content contents of the message
     */
    Message(final DiscordUser user, final Channel channel, final Long guildId, final String content) {
        this.user = user;
        this.channel = channel;
        this.guildId = guildId;
        this.content = content;
    }

    public Channel getChannel() {
        return channel;
    }

    public DiscordUser getUser() {
        return user;
    }

    public Long getGuildId() {
        return guildId;
    }

    public String getContent() {
        return content;
    }
}
