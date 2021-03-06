package ideaeclipse.DiscordAPI.bot.objects.channel.directMessage;

import ideaeclipse.DiscordAPI.bot.DiscordBot;
import ideaeclipse.DiscordAPI.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPI.bot.objects.message.IMessage;
import ideaeclipse.DiscordAPI.bot.objects.user.IDiscordUser;

import java.util.List;
import java.util.Map;

/**
 * Parsed direct message channel into a class object
 *
 * @author Ideaeclipse
 * @see CreateDMChannel
 * @see IChannel
 */
public final class DMChannel extends IChannel {
    private final long id;
    private final int type;
    private final List<IDiscordUser> recipients;

    /**
     * @param id         channel id
     * @param type       channel type {1}
     * @param recipients list of users that are apart of the direct message channel
     */
    DMChannel(final DiscordBot bot, final long id, final int type, final List<IDiscordUser> recipients) {
        super(bot);
        this.id = id;
        this.type = type;
        this.recipients = recipients;
    }

    @Override
    public boolean isNsfw() {
        return false;
    }

    @Override
    public String getName() {
        return "Dm Channel";
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
        return this.recipients;
    }

    @Override
    public Map<Long, IMessage> getMessageHistory() {
        return null;
    }

    @Override
    public void addMessage(IMessage message) {
    }

    @Override
    public String toString() {
        return "{DM Channel} " + " Id: " + this.id + " Type: " + this.type;
    }
}
