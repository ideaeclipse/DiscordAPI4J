package ideaeclipse.DiscordAPINEW.bot.objects.channel.regularChannels;

import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;

import java.util.List;

public class Channel extends IChannel {
    private final boolean nsfw;
    private final String name;
    private final long id;
    private final int type;

    public Channel(final boolean nsfw, final String name, final long id, final int type) {
        this.nsfw = nsfw;
        this.name = name;
        this.id = id;
        this.type = type;
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
    public List<IDiscordUser> getReciepient() {
        return null;
    }

    @Override
    public String toString() {
        return "{Channel} Nsfw: " + this.nsfw + " Name: " + this.name + " Id: " + this.id + " Type: " + this.type;
    }
}
