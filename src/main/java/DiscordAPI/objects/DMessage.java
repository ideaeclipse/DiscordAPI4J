package DiscordAPI.objects;

public class DMessage {
    private DUser user;
    private DChannel channel;
    private Long guildId;
    private String content;

    public DMessage(DUser user, DChannel channel, Long guildId, String content) {
        this.user = user;
        this.channel = channel;
        this.guildId = guildId;
        this.content = content;
    }

    public DChannel getChannel() {
        return channel;
    }

    public DUser getUser() {
        return user;
    }

    public Long getGuildId() {
        return guildId;
    }

    public String getContent() {
        return content;
    }
}
