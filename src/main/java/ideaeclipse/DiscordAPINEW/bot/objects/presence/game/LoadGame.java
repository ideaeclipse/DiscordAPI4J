package ideaeclipse.DiscordAPINEW.bot.objects.presence.game;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPINEW.utils.Util;
import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;
import ideaeclipse.reflectionListener.EventManager;

@JsonFormat
/**
 * TODO: add potential connections to load spotify songs based on info gather from payload
 *
 * Example json string for game.
 *
 * Name is the program
 * State is the action of the program
 * Details is the main info
 * additionalDetails is image text inside assets
 * type is the type, 0 is playing,1 is streaming, 2 is listening
 *{
 *   "assets": {
 *     "large_image": "spotify:444e1a72c2893bfdb30516f523309d1a1eaf384f",
 *     "large_text": "I Want To Die In New Orleans"
 *   },
 *   "timestamps": {
 *     "start": 1545188757894,
 *     "end": 1545188899865
 *   },
 *   "name": "Spotify",
 *   "flags": 48,
 *   "session_id": "b656fcb82955a8bcb99927699004c28c",
 *   "created_at": 1545188769694,
 *   "details": "Phantom Menace",
 *   "state": "$uicideBoy$",
 *   "id": "spotify:1",
 *   "type": 2,
 *   "sync_id": "6oyeeA0sHwHM1pPpZaIsrD",
 *   "party": {
 *     "id": "spotify:304408618986504195"
 *   }
 * }
 *
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPINEW.webSocket.DispatchType#PRESENCE_UPDATE}
 *
 * @author Ideaeclipse
 * @see IGame
 * @see Game
 * @see ideaeclipse.DiscordAPINEW.bot.objects.presence.PresenceUpdate
 */
public class LoadGame extends Event {
    /**
     * {@link Util#check(EventManager, Event, Json)} validates json components
     * parses information into an {@link IGame} object
     *
     * @param json json from websocket {@link ideaeclipse.DiscordAPINEW.webSocket.Wss}
     * @return game object
     */
    public IGame initialize(@JsonValidity( {"assets", "state", "details", "type"}) Json json) {
        return new Game(String.valueOf(json.get("name"))
                , String.valueOf(json.get("state"))
                , String.valueOf(json.get("details"))
                , String.valueOf(Util.check(this, "additionalText", new Json(String.valueOf(json.get("assets")))).getObject())
                , Integer.parseInt(String.valueOf(json.get("type"))));
    }

    /**
     * Parses the words attached to the image in the users presence
     *
     * @param json sub json 'assets'
     * @return addition details
     */
    public String additionalText(@JsonValidity( "large_text") Json json) {
        return String.valueOf(json.get("large_text"));
    }
}
