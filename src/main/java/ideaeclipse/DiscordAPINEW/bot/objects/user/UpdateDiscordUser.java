package ideaeclipse.DiscordAPINEW.bot.objects.user;

import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.role.IRole;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

import java.util.Map;

public class UpdateDiscordUser extends Event implements IDiscordAction {
    private final Map<Long, IRole> roles;
    private IDiscordUser user;

    public UpdateDiscordUser(final Map<Long, IRole> roles) {
        this.roles = roles;
    }

    @Override
    public void initialize(@JsonValidity({"nick", "user", "roles"}) Json json) {
        LoadUser user = new LoadUser(this.roles);
        Util.check(user,json);
        this.user = user.getUser();
    }

    public IDiscordUser getUser() {
        return user;
    }
}
