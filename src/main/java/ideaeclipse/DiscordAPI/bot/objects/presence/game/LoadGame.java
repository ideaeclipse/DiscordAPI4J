package ideaeclipse.DiscordAPI.bot.objects.presence.game;

import com.fasterxml.jackson.annotation.JsonFormat;
import ideaeclipse.DiscordAPI.bot.IPrivateBot;
import ideaeclipse.DiscordAPI.utils.Util;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

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
 * Called when a payload with the identifier of {@link ideaeclipse.DiscordAPI.webSocket.DispatchType#PRESENCE_UPDATE}
 *
 * @author Ideaeclipse
 * @see IGame
 * @see Game
 * @see ideaeclipse.DiscordAPI.bot.objects.presence.PresenceUpdate
 */
public class LoadGame extends Event {
    private final IPrivateBot bot;
    private final IGame game;

    /**
     * {@link Util#checkConstructor(Class, Json, IPrivateBot)} validates json components
     * parses information into an {@link IGame} object
     *
     * @param json json from websocket {@link ideaeclipse.DiscordAPI.webSocket.Wss}
     */
    private LoadGame(@JsonValidity({"assets", "state", "details", "type"}) final Json json, final IPrivateBot bot) {
        this.bot = bot;
        this.game = new Game(String.valueOf(json.get("name"))
                , String.valueOf(json.get("state"))
                , String.valueOf(json.get("details"))
                , String.valueOf(Util.check(this, "additionalText", new Json(String.valueOf(json.get("assets")))).getObject())
                , GameType.values()[Integer.parseInt(String.valueOf(json.get("type")))]);
    }

    /**
     * Parses the words attached to the image in the users presence
     *
     * @param json sub json 'assets'
     * @return addition details
     */
    public String additionalText(@JsonValidity("large_text") Json json) {
        return String.valueOf(json.get("large_text"));
    }

    /**
     * @return returns game object
     */
    public IGame getGame() {
        return game;
    }
}
