package ideaeclipse.DiscordAPINEW.bot.objects.user;

import ideaeclipse.DiscordAPINEW.bot.objects.role.IRole;

import java.util.List;

public interface IDiscordUser {
    String getUsername();

    int getDiscriminator();

    long getId();

    List<IRole> getRoles();
}
