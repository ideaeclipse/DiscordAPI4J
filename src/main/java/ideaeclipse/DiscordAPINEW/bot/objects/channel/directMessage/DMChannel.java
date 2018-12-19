package ideaeclipse.DiscordAPINEW.bot.objects.channel.directMessage;

import ideaeclipse.DiscordAPINEW.bot.objects.channel.IChannel;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;

import java.util.List;

public class DMChannel extends IChannel {
    private final long id;
    private final int type;
    private final List<IDiscordUser> reciepients;
    DMChannel(final long id, final int type, final List<IDiscordUser> reciepient){
        this.id = id;
        this.type = type;
        this.reciepients = reciepient;
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
    public List<IDiscordUser> getReciepient() {
        return this.reciepients;
    }
}
