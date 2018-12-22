package ideaeclipse.DiscordAPINEW.bot.objects.role;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;
import ideaeclipse.reflectionListener.EventManager;

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
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPINEW.webSocket.DispatchType#GUILD_ROLE_UPDATE}
 *
 * @author Ideaeclipse
 * @see Role
 * @see IRole
 * @see CreateRole
 * @see DeleteRole
 * @see ideaeclipse.DiscordAPINEW.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class UpdateRole extends Event implements IDiscordAction {
    private IRole role;

    /**
     * {@link Util#check(EventManager, Event, Json)} validates inputted json string
     * uses {@link CreateRole} to parse json
     *
     * @param json json inputted from {@link ideaeclipse.DiscordAPINEW.webSocket.Wss}
     */
    @Override
    public void initialize(@JsonValidity( {"role"}) Json json) {
        CreateRole role = new CreateRole();
        Util.check(role, new Json(String.valueOf(json.get("role"))));
        this.role = role.getRole();
    }

    /**
     * @return parsed role object
     */
    public IRole getRole() {
        return role;
    }
}
