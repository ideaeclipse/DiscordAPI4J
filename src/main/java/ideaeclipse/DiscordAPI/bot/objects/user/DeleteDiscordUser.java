package ideaeclipse.DiscordAPI.bot.objects.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPI.bot.DiscordBot;
import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.parents.Event;

@JsonFormat
/**
 * This object is called when a guild member remove payload is sent to {@link Wss}
 * It will remove the user pased on id from the users map
 *
 * Json Example:
 *
 * Only search for 'id' under the sub json 'user'
 *
 * {
 *   "guild_id": "471104815192211458",
 *   "user": {
 *     "id": "525130696008663041",
 *     "avatar": null,
 *     "username": "bigMemes2k18",
 *     "discriminator": "4013"
 *   }
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#GUILD_MEMBER_REMOVE}
 *
 * @author Ideaeclipse
 * @see CreateDiscordUser
 * @see UpdateDiscordUser
 * @see IDiscordUser
 * @see Wss#Wss(IDiscordBot, String)
 */
public final class DeleteDiscordUser extends Event {
    private final DiscordBot bot;
    private final IDiscordUser user;

    /**
     * {@link Util#checkConstructor(Class, Json, DiscordBot)} validates json string for components
     * calls {@link #parse(Json)} to get id and then remove the key value pair from users with that id
     *
     * @param json json string from {@link Wss}
     */
    private DeleteDiscordUser(@JsonValidity("user") final Json json, final DiscordBot bot) {
        this.bot = bot;
        this.user = IDiscordUser.parse(Util.check(this, "parse", new Json(String.valueOf(json.get("user")))).getObject());
    }

    /**
     * @param json parses 'id' from sub json 'user' and removes it from the users map
     */
    public IDiscordUser parse(@JsonValidity("id") Json json) {
        long id = Long.parseUnsignedLong(String.valueOf(json.get("id")));
        IDiscordUser user = this.bot.getUsers().getByK1(id);
        this.bot.getUsers().removeByK1(id);
        return user;
    }

    /**
     * @return object of deleted user
     */
    public IDiscordUser getUser() {
        return user;
    }
}
