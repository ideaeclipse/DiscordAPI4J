package ideaeclipse.DiscordAPI.bot.objects.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPI.bot.DiscordBot;
import ideaeclipse.DiscordAPI.bot.IDiscordBot;
import ideaeclipse.DiscordAPI.webSocket.Wss;
import ideaeclipse.DiscordAPI.bot.objects.role.IRole;
import ideaeclipse.DiscordAPI.utils.MultiKeyMap;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.parents.Event;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonFormat
/**
 * TODO: remove user and nickname variables. Make methods that parse them and return them.
 * This class is called when a guild member add payload is sent to {@link Wss}
 *
 * Json Example
 *
 * {
 *   "nick": null,
 *   "joined_at": "2018-12-20T02:04:55.063206+00:00",
 *   "roles": [
 *
 *   ],
 *   "guild_id": "471104815192211458",
 *   "deaf": false,
 *   "mute": false,
 *   "user": {
 *     "id": "525130696008663041",
 *     "avatar": null,
 *     "username": "bigMemes2k18",
 *     "discriminator": "4013"
 *   }
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#GUILD_MEMBER_ADD}
 *
 * @author Ideaeclipse
 * @see UpdateDiscordUser
 * @see DeleteDiscordUser
 * @see IDiscordUser
 * @see Wss#Wss(IDiscordBot, String)
 */
public final class CreateDiscordUser extends Event {
    private final DiscordBot bot;
    private final MultiKeyMap<Long, String, IRole> userRoles = new MultiKeyMap<>();
    private IDiscordUser user;
    private String nickname;

    /**
     * {@link Util#checkConstructor(Class, Json, DiscordBot)} validates json components
     * parses roles from the map of roles, and parses nickname
     *
     * @param json json from {@link Wss}
     */
    private CreateDiscordUser(@JsonValidity({"user", "roles"}) final Json json, final DiscordBot bot) {
        this.bot = bot;
        String str = String.valueOf(json.get("roles"));
        List<String> strings = Arrays.asList(str.substring(1, str.length() - 1).trim().split("\\s*,\\s*"));
        strings = strings.stream().filter(o -> o.length() > 0).collect(Collectors.toList());
        for (String string : strings) {
            Long l = Long.parseUnsignedLong(string.replace("\"", ""));
            IRole role = this.bot.getRoles().getByK1(l);
            userRoles.put(l, role.getName(), role);
        }
        if (json.getMap().keySet().contains("nick"))
            this.nickname = String.valueOf(json.get("nick"));
        Util.check(this, "load", new Json(String.valueOf(json.get("user"))));
    }

    /**
     * Parses the user data from the sub json 'user'
     *
     * @param json sub json 'user'
     */
    private void load(@JsonValidity({"username", "discriminator", "id"}) Json json) {
        this.user = new DiscordUser(this.bot, this.nickname,
                String.valueOf(json.get("username")),
                Integer.parseInt(String.valueOf(json.get("discriminator"))),
                Long.parseUnsignedLong(String.valueOf(json.get("id"))),
                this.userRoles);
    }

    /**
     * @return user object that was created
     */
    public IDiscordUser getUser() {
        return user;
    }
}
