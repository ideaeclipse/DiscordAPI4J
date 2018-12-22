package ideaeclipse.DiscordAPINEW.bot.objects.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
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
public class DeleteRole extends Event implements IDiscordAction {
    private final Map<Long, IRole> roles;

    /**
     * @param roles map of roles for removal
     */
    public DeleteRole(final Map<Long, IRole> roles) {
        this.roles = roles;
    }

    /**
     * {@link ideaeclipse.DiscordAPINEW.utils.Util#check(EventManager, Event, Json)} validates json components
     * parses 'role_id' into a long and removes that key value pair from the roles map
     *
     * @param json json from {@link ideaeclipse.DiscordAPINEW.webSocket.Wss}
     */
    @Override
    public void initialize(@JsonValidity( "role_id") Json json) {
        roles.remove(Long.parseUnsignedLong(String.valueOf(json.get("role_id"))));
    }
}
