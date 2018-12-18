package ideaeclipse.DiscordAPINEW.bot.objects.presence;


import ideaeclipse.DiscordAPINEW.bot.objects.presence.game.IGame;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;

public class Presence implements IPresence {
    private final StatusTypes status;
    private final IDiscordUser user;

    Presence(final StatusTypes status, final IDiscordUser user) {
        this.status = status;
        this.user = user;
    }

    @Override
    public StatusTypes getStatus() {
        return this.status;
    }

    @Override
    public IDiscordUser getUser() {
        return this.user;
    }

    @Override
    public IGame getActivity() {
        return null;
    }
}
