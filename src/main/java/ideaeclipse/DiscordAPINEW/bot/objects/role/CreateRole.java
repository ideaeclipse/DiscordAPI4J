package ideaeclipse.DiscordAPINEW.bot.objects.role;

import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

public class CreateRole extends Event implements IDiscordAction {
    private IRole role;

    @Override
    public void initialize(@JsonValidity(value = {"color", "managed", "permissions", "name", "mentionable", "position", "id", "hoist"}) Json json) {
        this.role = new Role(Integer.parseInt(String.valueOf(json.get("color")))
                , Boolean.parseBoolean(String.valueOf(json.get("managed")))
                , Integer.parseInt(String.valueOf(json.get("permissions")))
                , String.valueOf(json.get("name"))
                , Boolean.parseBoolean(String.valueOf(json.get("mentionable")))
                , Integer.parseInt(String.valueOf(json.get("position")))
                , Long.parseUnsignedLong(String.valueOf(json.get("id")))
                , Boolean.parseBoolean(String.valueOf(json.get("hoist"))));
    }

    public IRole getRole() {
        return role;
    }
}
