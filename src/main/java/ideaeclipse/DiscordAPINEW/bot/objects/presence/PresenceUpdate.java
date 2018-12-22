package ideaeclipse.DiscordAPINEW.bot.objects.presence;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.bot.IPrivateBot;
import ideaeclipse.DiscordAPINEW.bot.objects.IDiscordAction;
import ideaeclipse.DiscordAPINEW.bot.objects.presence.game.IGame;
import ideaeclipse.DiscordAPINEW.bot.objects.presence.game.LoadGame;
import ideaeclipse.DiscordAPINEW.bot.objects.user.IDiscordUser;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;
import ideaeclipse.reflectionListener.EventManager;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JsonFormat
/**
 * This class parses a presence update payload into a presence class and potentially a game class
 *
 * Example json string
 *
 * Game is parsed by {@link ideaeclipse.DiscordAPINEW.bot.objects.presence.game.LoadGame}
 * user is parsed for the id and grabbed from {@link IPrivateBot#getUsers()}
 * status is the users status
 * {
 *   "game": {
 *     "assets": {
 *       "large_image": "403245045018525696",
 *       "large_text": "Talon"
 *     },
 *     "timestamps": {
 *       "start": 1545183893000
 *     },
 *     "name": "League of Legends",
 *     "created_at": 1545183893606,
 *     "state": "In Game",
 *     "id": "123f759f91ca980f",
 *     "type": 0,
 *     "application_id": "401518684763586560"
 *   },
 *   "client_status": {
 *     "desktop": "online"
 *   },
 *   "activities": [
 *     {
 *       "assets": {
 *         "large_image": "403245045018525696",
 *         "large_text": "Talon"
 *       },
 *       "timestamps": {
 *         "start": 1545183893000
 *       },
 *       "name": "League of Legends",
 *       "created_at": 1545183893606,
 *       "state": "In Game",
 *       "id": "123f759f91ca980f",
 *       "type": 0,
 *       "application_id": "401518684763586560"
 *     }
 *   ],
 *   "roles": [
 *
 *   ],
 *   "guild_id": "471104815192211458",
 *   "user": {
 *     "id": "226469650903334934"
 *   },
 *   "status": "online"
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPINEW.webSocket.DispatchType#PRESENCE_UPDATE}
 *
 * @author Ideaeclipse
 * @see IPresence
 * @see IGame
 * @see Presence
 * @see LoadGame
 * @see StatusTypes
 * @see ideaeclipse.DiscordAPINEW.webSocket.Wss#Wss(IPrivateBot, String)
 */
public class PresenceUpdate extends Event implements IDiscordAction {
    private final Map<Long, IDiscordUser> users;
    private IPresence presence;

    /**
     * @param users users map from {@link IPrivateBot}
     */
    public PresenceUpdate(final Map<Long, IDiscordUser> users) {
        this.users = users;
    }

    /**
     * {@link Util#check(EventManager, Event, Json)} validates json object has right keys
     * Filters {@link StatusTypes} to get a enum value instead of a string
     *
     * @param json json from {@link ideaeclipse.DiscordAPINEW.webSocket.Wss}
     * @see StatusTypes
     */
    @Override
    public void initialize(@JsonValidity( {"game", "status", "user"}) Json json) {
        List<StatusTypes> filtered = Arrays.stream(StatusTypes.values()).filter(o -> o.name().toLowerCase().equals(String.valueOf(json.get("status")).toLowerCase())).collect(Collectors.toList());
        this.presence = new Presence(!filtered.isEmpty() ? filtered.get(0) : StatusTypes.invisible
                , IDiscordUser.parse(Util.check(this, "loadUsers", new Json(String.valueOf(json.get("user")))).getObject())
                , !String.valueOf(json.get("game")).equals("null") ? IGame.parse(Util.check(new LoadGame(), new Json(String.valueOf(json.get("game")))).getObject()) : null);
    }

    /**
     * Searches through sub json user to find the id and then user the map to get a user object
     * Id will always be a long.
     *
     * @param json sub json user
     * @return user object from users using the found id
     */
    public IDiscordUser loadUsers(@JsonValidity( {"id"}) Json json) {
        return this.users.get(Long.parseUnsignedLong(String.valueOf(json.get("id"))));
    }

    /**
     * @return created presence object
     */
    public IPresence getPresence() {
        return presence;
    }
}
