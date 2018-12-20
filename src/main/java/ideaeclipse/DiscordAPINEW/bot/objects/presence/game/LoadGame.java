package ideaeclipse.DiscordAPINEW.bot.objects.presence.game;

import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

/**
 * @author Ideaeclipse
 * @see IGame
 * @see Game
 * @see ideaeclipse.DiscordAPINEW.bot.objects.presence.PresenceUpdate
 */
public class LoadGame extends Event {
    public IGame initialize(@JsonValidity(value = {"assets", "state", "details", "type"}) Json json) {
        return new Game(String.valueOf(json.get("name"))
                , String.valueOf(json.get("state"))
                , String.valueOf(json.get("details"))
                , String.valueOf(Util.check(this,"additionalText",new Json(String.valueOf(json.get("assets")))).getObject())
                , Integer.parseInt(String.valueOf(json.get("type"))));
    }

    public String additionalText(@JsonValidity(value = "large_text") Json json) {
        return String.valueOf(json.get("large_text"));
    }
}
