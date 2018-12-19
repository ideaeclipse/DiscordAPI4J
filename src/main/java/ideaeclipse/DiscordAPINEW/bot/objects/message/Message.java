package ideaeclipse.DiscordAPINEW.bot.objects.message;

import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;

public class Message implements IMessage {
    private final String content;
    private final IChannel channel;
    private final IDiscordUser user;

    Message(final String content, final IChannel channel, final IDiscordUser user) {
        this.content = content;
        this.channel = channel;
        this.user = user;
        System.out.println(toString());
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public IChannel getChannel() {
        return this.channel;
    }

    @Override
    public IDiscordUser getUser() {
        return this.user;
    }

    @Override
    public String toString() {
        return (this.user != null ? "{Message} User: " + this.user.getUsername() : "") + (this.channel != null ? " Channel: " + this.channel.getName() : "") + " Content: " + this.content;
    }
}