package ideaeclipse.DiscordAPI.bot.objects.channel.regularChannels;

import ideaeclipse.DiscordAPI.bot.DiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPI.webSocket.Wss;

import java.util.List;
import java.util.Map;

/**
 * Channel data mapped to an object from {@link Wss}
 *
 * @author Ideaeclipse
 * @see CreateChannel
 * @see UpdateChannel
 * @see IChannel
 */
public final class Channel extends IChannel {
    private final boolean nsfw;
    private final String name;
    private final long id;
    private final int type;
    private final Map<Long, IMessage> messageHistory;

    /**
     * @param nsfw boolean on channels nsfw status
     * @param name name of channel
     * @param id   channel unique identifier
     * @param type channel type 0: text 2: voice 4: group
     */
    Channel(final DiscordBot bot, final boolean nsfw, final String name, final long id, final int type, final Map<Long, IMessage> messageHistory) {
        super(bot);
        this.nsfw = nsfw;
        this.name = name;
        this.id = id;
        this.type = type;
        this.messageHistory = messageHistory;
    }

    @Override
    public boolean isNsfw() {
        return this.nsfw;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public List<IDiscordUser> getReciepients() {
        return null;
    }

    @Override
    public Map<Long, IMessage> getMessageHistory() {
        return this.messageHistory;
    }

    /**
     * @param message message Object to add to history
     */
    public void addMessage(final IMessage message) {
        this.messageHistory.put(message.getId(), message);
    }

    @Override
    public String toString() {
        return "{Channel} Nsfw: " + this.nsfw + " Name: " + this.name + " Id: " + this.id + " Type: " + this.type;
    }
}
