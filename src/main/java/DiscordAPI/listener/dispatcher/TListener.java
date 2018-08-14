package DiscordAPI.listener.dispatcher;


import DiscordAPI.listener.listenerTypes.ListenerEvent;

/**
 * Used for listener library
 *
 * @param <T>
 * @author Myles
 */
public interface TListener<T extends ListenerEvent> {
    /**
     * @param event new Instance of T allows for use of custom methods per event
     */
    void handle(final T event);
}
