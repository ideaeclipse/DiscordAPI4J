package ideaeclipse.DiscordAPINEW.bot.objects.presence;

import ideaeclipse.DiscordAPINEW.bot.objects.presence.game.IGame;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;

public interface IPresence {
    StatusTypes getStatus();

    IDiscordUser getUser();

    IGame getActivity();
}
