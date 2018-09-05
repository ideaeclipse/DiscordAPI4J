package DiscordAPI.listener.discordApiListener;

import DiscordAPI.listener.discordApiListener.listenerTypes.ListenerEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Dispatcher is used to handle all listeners initialized by the user
 * This object is stored in {@link DiscordAPI.objects.DiscordBot} {@link DiscordAPI.IDiscordBot}
 *
 * @author Ideaeclipse
 */
public class IDispatcher {
    private List<IListener<?>> listeners;

    /**
     * Initializes the List of Listeners
     */
    public IDispatcher() {
        listeners = new ArrayList<>();
    }

    /**
     * @param listener is an instance of IListener {@link IListener}
     * @param <T>      is a type that extends ListenerEvent {@link ListenerEvent}
     */
    public <T extends ListenerEvent> void addListener(final IListener<T> listener) {
        listeners.add(listener);
    }

    /**
     * @param listenerType is a new instance of a class that extends ListenerEvent
     */
    public void notify(final ListenerEvent listenerType) {
        for (IListener ev : listeners) {
            try {
                ev.handle(listenerType);
            } catch (ClassCastException ignored) {

            }
        }
    }
}
