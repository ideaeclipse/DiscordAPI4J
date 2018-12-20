package ideaeclipse.DiscordAPINEW.utils;

import java.util.function.Consumer;

public class CheckResponse<T> {
    private final CheckResponeType type;
    private final T object;

    public CheckResponse(final CheckResponeType type, final T object) {
        this.type = type;
        this.object = object;
    }

    public CheckResponeType getType() {
        return this.type;
    }

    public T getObject() {
        return this.object;
    }

    public void ifPresent(final Consumer<? super T> action) {
        if (this.object != null)
            action.accept(object);
    }

}
