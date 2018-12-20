package ideaeclipse.DiscordAPINEW.bot.objects.presence;

import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.presence.game.IGame;
import ideaeclipse.DiscordAPINEW.bot.objects.presence.game.LoadGame;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PresenceUpdate extends Event implements IDiscordAction {
    private final Map<Long, IDiscordUser> users;
    private IPresence presence;

    public PresenceUpdate(final Map<Long, IDiscordUser> users) {
        this.users = users;
    }

    @Override
    public void initialize(@JsonValidity(value = {"game", "status", "user"}) Json json) {
        List<StatusTypes> filtered = Arrays.stream(StatusTypes.values()).filter(o -> o.name().toLowerCase().equals(String.valueOf(json.get("status")).toLowerCase())).collect(Collectors.toList());
        this.presence = new Presence(!filtered.isEmpty() ? filtered.get(0) : StatusTypes.invisible
                , IDiscordUser.parse(Util.check(this, "loadUsers", new Json(String.valueOf(json.get("user")))).getObject())
                , !String.valueOf(json.get("game")).equals("null") ? IGame.parse(Util.check(new LoadGame(), new Json(String.valueOf(json.get("game")))).getObject()) : null);
    }

    public IDiscordUser loadUsers(@JsonValidity(value = {"id"}) Json json) {
        return this.users.get(Long.parseUnsignedLong(String.valueOf(json.get("id"))));
    }

    public IPresence getPresence() {
        return presence;
    }
}
