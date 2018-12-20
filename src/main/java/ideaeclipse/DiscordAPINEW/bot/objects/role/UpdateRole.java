package ideaeclipse.DiscordAPINEW.bot.objects.role;

import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

public class UpdateRole extends Event implements IDiscordAction {
    private IRole role;

    @Override
    public void initialize(@JsonValidity(value = {"role"}) Json json) {
        CreateRole role = new CreateRole();
        Util.check(role, new Json(String.valueOf(json.get("role"))));
        this.role = role.getRole();
    }

    public IRole getRole() {
        return role;
    }
}
