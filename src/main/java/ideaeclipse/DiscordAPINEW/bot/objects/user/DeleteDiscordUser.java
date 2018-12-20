package ideaeclipse.DiscordAPINEW.bot.objects.user;

import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

import java.util.Map;

public class DeleteDiscordUser extends Event implements IDiscordAction {
    private final Map<Long,IDiscordUser> users;
    public DeleteDiscordUser(final Map<Long, IDiscordUser> users){
        this.users = users;
    }
    @Override
    public void initialize(@JsonValidity(value = "user") Json json) {
        Util.check(this,"parse",new Json(String.valueOf(json.get("user"))));
    }
    public void parse(@JsonValidity(value = "id") Json json){
        users.remove(Long.parseUnsignedLong(String.valueOf(json.get("id"))));
    }
}
