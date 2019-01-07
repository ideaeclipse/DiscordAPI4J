package ideaeclipse.DiscordAPI.bot.objects.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPI.bot.IPrivateBot;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

@JsonFormat
/**
 * Called when an updated role payload is called.
 * Uses {@link CreateRole} to parse role object. Once the role object gets created
 * The old role object will be replaced with the updated one
 *
 * Json example:
 *
 * {
 *   "role": {
 *     "color": 15158332,
 *     "permissions": 104324161,
 *     "managed": false,
 *     "name": "memes",
 *     "mentionable": false,
 *     "position": 1,
 *     "id": "525059526345490442",
 *     "hoist": true
 *   },
 *   "guild_id": "471104815192211458"
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#GUILD_ROLE_UPDATE}
 *
 * @author Ideaeclipse
 * @see Role
 * @see IRole
 * @see CreateRole
 * @see DeleteRole
 * @see ideaeclipse.DiscordAPI.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class UpdateRole extends Event {
    private final IPrivateBot bot;
    private final IRole role;

    /**
     * {@link Util#checkConstructor(Class, Json, IPrivateBot)}  validates inputted json string
     * uses {@link CreateRole} to parse json
     *
     * @param json json inputted from {@link ideaeclipse.DiscordAPI.webSocket.Wss}
     */
    private UpdateRole(@JsonValidity({"role"}) final Json json, final IPrivateBot bot) {
        this.bot = bot;
        CreateRole role = Util.checkConstructor(CreateRole.class, new Json(String.valueOf(json.get("role"))), bot).getObject();
        this.role = role.getRole();
    }

    /**
     * @return parsed role object
     */
    public IRole getRole() {
        return role;
    }
}
