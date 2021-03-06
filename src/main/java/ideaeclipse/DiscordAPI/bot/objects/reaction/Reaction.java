package ideaeclipse.DiscordAPI.bot.objects.reaction;

import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;

/**
 * Used to store reaction data
 *
 * @author Ideaeclipse
 * @see IReaction
 * @see AddReaction
 * @see RemoveReaction
 */
final class Reaction implements IReaction {
    private final String code;
    private final String emoji;
    private final IChannel channel;
    private final IMessage message;

    /**
     * @param code emoji code
     * @param emoji literal emoji
     * @param channel channel object
     * @param message message object
     */
    Reaction(final String code, final String emoji, final IChannel channel, final IMessage message) {
        this.code = code;
        this.emoji = emoji;
        this.channel = channel;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getEmoji() {
        return this.emoji;
    }

    @Override
    public IChannel getChannel() {
        return this.channel;
    }

    @Override
    public IMessage getMessage() {
        return this.message;
    }


    @Override
    public String toString() {
        return "{Reaction} name: " + this.code + " emoji: " + this.emoji + " channel: " + this.channel.getName() + " message: " + this.message.getContent();
    }
}
