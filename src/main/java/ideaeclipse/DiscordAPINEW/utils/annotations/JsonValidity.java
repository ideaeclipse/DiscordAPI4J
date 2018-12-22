package ideaeclipse.DiscordAPINEW.utils.annotations;

import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to stores values in which a json object should contain.
 * If it doesn't contain those values it wont be executed
 *
 * @author Ideaeclipse
 * @see ideaeclipse.DiscordAPINEW.webSocket.Wss
 * @see ideaeclipse.DiscordAPINEW.utils.Util#check(Event, String, Json)
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.PARAMETER)
public @interface JsonValidity {
    String[] value();
}
