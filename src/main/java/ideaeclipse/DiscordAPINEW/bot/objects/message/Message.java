package ideaeclipse.DiscordAPINEW.bot.objects.message;

import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;

import java.util.HashMap;
import java.util.Map;

/**
 * Object that represents a message sent in a text channel
 *
 * @author Ideaeclipse
 * @see MessageCreate
 * @see IMessage
 * @see IChannel
 * @see IDiscordUser
 */
public class Message implements IMessage {
    private final long id;
    private final String content;
    private final IChannel channel;
    private final IDiscordUser user;
    private final Map<String, Integer> reactionMap;

    /**
     * @param content message content
     * @param channel channel message was sent in
     * @param user    user who sent the message
     */
    Message(final long id, final String content, final IChannel channel, final IDiscordUser user, final Map<String,Integer> reactionMap) {
        this.id = id;
        this.content = content;
        this.channel = channel;
        this.user = user;
        this.reactionMap = reactionMap;
    }

    @Override
    public long getId() {
        return this.id;
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
    public Map<String, Integer> getReactions() {
        return this.reactionMap;
    }

    @Override
    public void addReaction(String emoji) {
        this.reactionMap.merge(emoji, 1, (a, b) -> a + b);
    }

    @Override
    public void removeReaction(String emoji) {
        if (this.reactionMap.get(emoji) > 1)
            this.reactionMap.put(emoji, this.reactionMap.get(emoji) - 1);
        else
            this.reactionMap.remove(emoji);
    }

    @Override
    public String toString() {
        return (this.user != null ? "{Message} User: " + this.user.getUsername() : "") + (this.channel != null ? " Channel: " + this.channel.getName() : "") + " Content: " + this.content;
    }
}
