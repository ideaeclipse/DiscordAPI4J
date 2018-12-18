package ideaeclipse.DiscordAPINEW.bot.objects.user;

import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.role.IRole;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

import java.util.*;
import java.util.stream.Collectors;

public class LoadUser extends Event implements IDiscordAction {
    private IDiscordUser user;
    private final List<IRole> userRoles = new LinkedList<>();
    private final Map<Long, IRole> roles;

    public LoadUser(final Map<Long, IRole> roles) {
        this.roles = roles;
    }

    @Override
    public void initialize(@JsonValidity(value = {"user", "roles"}) Json json) {
        String str = String.valueOf(json.get("roles"));
        List<String> strings = Arrays.asList(str.substring(1, str.length() - 1).trim().split("\\s*,\\s*"));
        strings = strings.stream().filter(o -> o.length() > 0).collect(Collectors.toList());
        for (String string : strings) {
            Long l = Long.parseUnsignedLong(string.replace("\"", ""));
            userRoles.add(roles.get(l));
        }
        Util.check(this, "load", new Json(String.valueOf(json.get("user"))));
    }

    private void load(@JsonValidity(value = {"username", "discriminator", "id"}) Json json) {
        this.user = new DiscordUser(String.valueOf(json.get("username")),
                Integer.parseInt(String.valueOf(json.get("discriminator"))),
                Long.parseUnsignedLong(String.valueOf(json.get("id"))),
                this.userRoles);
    }

    public IDiscordUser getUser() {
        return user;
    }
}
