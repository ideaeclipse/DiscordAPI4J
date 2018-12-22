package ideaeclipse.DiscordAPINEW.utils;

import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;

import java.util.function.Consumer;

/**
 * Return value from {@link Util#check(Event, String, Json)}.
 *
 * @param <T> generic return value
 * @author Ideaeclipse
 * @see Util#check(Event, String, Json)
 */
public class CheckResponse<T> {
    private final CheckResponeType type;
    private final T object;

    /**
     * @param type {@link CheckResponeType}
     * @param object return object if any
     */
    CheckResponse(final CheckResponeType type, final T object) {
        this.type = type;
        this.object = object;
    }

    /**
     * @return type {@link CheckResponeType}
     */
    public CheckResponeType getType() {
        return this.type;
    }

    /**
     * Will only be not null if {@link #getType()} is {@link CheckResponeType#EXECUTED}
     * @return object if any
     */
    public T getObject() {
        return this.object;
    }

    /**
     * Execute a chunk of code if the object exists
     * @param action consumer
     */
    public void ifPresent(final Consumer<? super T> action) {
        if (this.object != null)
            action.accept(object);
    }

}
