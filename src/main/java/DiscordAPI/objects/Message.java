package DiscordAPI.objects;

public class Message {
    private final User user;
    private final Channel channel;
    private final Long guildId;
    private final String content;

    Message(final User user, final Channel channel, final Long guildId, final String content) {
        this.user = user;
        this.channel = channel;
        this.guildId = guildId;
        this.content = content;
    }

    public Channel getChannel() {
        return channel;
    }

    public User getUser() {
        return user;
    }

    public Long getGuildId() {
        return guildId;
    }

    public String getContent() {
        return content;
    }
}
