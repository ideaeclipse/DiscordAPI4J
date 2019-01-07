package ideaeclipse.DiscordAPI.bot.objects.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPI.bot.IPrivateBot;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

@JsonFormat
/**
 * This class is used parse a create role payload into a {@link IRole} object
 *
 * Json examples:
 *
 * When a {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#GUILD_ROLE_CREATE} payload is sent
 * key role will always be there.
 * All key's get parsed into the {@link Role} object
 *
 * {
 *   "role": {
 *     "color": 0,
 *     "permissions": 104324161,
 *     "managed": false,
 *     "name": "new role",
 *     "mentionable": false,
 *     "position": 1,
 *     "id": "525059526345490442",
 *     "hoist": false
 *   },
 *   "guild_id": "471104815192211458"
 * }
 *
 * When the bot calls the http address to query all roles
 *
 * {
 *   "color": 0,
 *   "managed": true,
 *   "permissions": 262216,
 *   "name": "Testing",
 *   "mentionable": false,
 *   "position": 1,
 *   "id": "472196871884898304",
 *   "hoist": false
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#GUILD_ROLE_CREATE}
 *
 * @author Ideaeclipse
 * @see Role
 * @see IRole
 * @see ideaeclipse.DiscordAPI.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class CreateRole extends Event {
    private final IPrivateBot bot;
    private final IRole role;

    /**
     * {@link ideaeclipse.DiscordAPI.utils.Util#checkConstructor(Class, Json, IPrivateBot)}  validates json string
     * Then parses all data into an {@link IRole} object
     *
     * @param json json from {@link ideaeclipse.DiscordAPI.webSocket.Wss}
     */
    private CreateRole(@JsonValidity({"color", "managed", "permissions", "name", "mentionable", "position", "id", "hoist"}) final Json json, final IPrivateBot bot) {
        this.bot = bot;
        this.role = new Role(Integer.parseInt(String.valueOf(json.get("color")))
                , Boolean.parseBoolean(String.valueOf(json.get("managed")))
                , Integer.parseInt(String.valueOf(json.get("permissions")))
                , String.valueOf(json.get("name"))
                , Boolean.parseBoolean(String.valueOf(json.get("mentionable")))
                , Integer.parseInt(String.valueOf(json.get("position")))
                , Long.parseUnsignedLong(String.valueOf(json.get("id")))
                , Boolean.parseBoolean(String.valueOf(json.get("hoist"))));
    }

    /**
     * @return return generated role from the payload
     */
    public IRole getRole() {
        return role;
    }
}
