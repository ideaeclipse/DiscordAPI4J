package ideaeclipse.DiscordAPINEW.bot.objects.role;

import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

import java.util.Map;

public class DeleteRole extends Event implements IDiscordAction {
    private final Map<Long, IRole> roles;

    public DeleteRole(final Map<Long, IRole> roles) {
        this.roles = roles;
    }

    @Override
    public void initialize(@JsonValidity(value = "role_id") Json json) {
        roles.remove(Long.parseUnsignedLong(String.valueOf(json.get("role_id"))));
    }
}
