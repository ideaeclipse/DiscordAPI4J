package ideaeclipse.DiscordAPI.utils;

import ideaeclipse.JsonUtilities.Json;

/**
 * Types of response from {@link Util#check(Event, String, Json)}
 *
 * @author Ideaeclipse
 * @see CheckResponse
 * @see Util#check(Event, String, Json)
 */
public enum CheckResponeType {
    EXECUTED,NOTEXECUTED,NOTFOUND
}
