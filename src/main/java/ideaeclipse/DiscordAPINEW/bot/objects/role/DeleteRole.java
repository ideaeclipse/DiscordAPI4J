package ideaeclipse.DiscordAPINEW.bot.objects.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;
import ideaeclipse.reflectionListener.EventManager;

import java.util.Map;

@JsonFormat
/**
 * This class is called when a delete role payload is passed through {@link ideaeclipse.DiscordAPINEW.webSocket.Wss}
 * Then map of roles will then be updated one the role id is found and parsed into a long
 *
 * Json Example
 *
 * {
 *   "role_id": "525117763044507648",
 *   "guild_id": "471104815192211458"
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPINEW.webSocket.DispatchType#GUILD_ROLE_DELETE}
 *
 * @author Ideaeclipse
 * @see ideaeclipse.DiscordAPINEW.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class DeleteRole extends Event {
    private final IPrivateBot bot;
    private final IRole role;

    /**
     * {@link ideaeclipse.DiscordAPINEW.utils.Util#checkConstructor(Class, Json, IPrivateBot)} validates json components
     * parses 'role_id' into a long and removes that key value pair from the roles map
     *
     * @param json json from {@link ideaeclipse.DiscordAPINEW.webSocket.Wss}
     */
    private DeleteRole(@JsonValidity("role_id") final Json json, final IPrivateBot bot) {
        this.bot = bot;
        long id = Long.parseUnsignedLong(String.valueOf(json.get("role_id")));
        this.role = this.bot.getRoles().get(id);
        this.bot.getRoles().remove(id);
    }

    /**
     * @return deleted role object
     */
    public IRole getRole() {
        return this.role;
    }
}
