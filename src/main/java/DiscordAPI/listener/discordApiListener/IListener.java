package DiscordAPI.listener.discordApiListener;


import DiscordAPI.listener.discordApiListener.listenerTypes.ListenerEvent;

/**
 * Used for listener library
 *
 * @param <T>
 * @author Ideaeclipse
 */
public interface IListener<T extends ListenerEvent> {
    /**
     * @param event new Instance of T allows for use of custom methods per event
     */
    void handle(final T event);
}
