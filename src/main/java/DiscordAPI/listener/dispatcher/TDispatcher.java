package DiscordAPI.listener.dispatcher;

import DiscordAPI.listener.listenerTypes.ListenerEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Dispatcher is used to handle all listeners initialized by the user
 * This object is stored in {@link DiscordAPI.objects.DiscordBot} {@link DiscordAPI.IDiscordBot}
 *
 * @author Myles
 */
public class TDispatcher {
    private List<TListener<?>> listeners;

    /**
     * Initializes the List of Listeners
     */
    public TDispatcher() {
        listeners = new ArrayList<>();
    }

    /**
     * @param listener is an instance of TListener {@link TListener}
     * @param <T>      is a type that extends ListenerEvent {@link ListenerEvent}
     */
    public <T extends ListenerEvent> void addListener(final TListener<T> listener) {
        listeners.add(listener);
    }

    /**
     * @param listenerType is a new instance of a class that extends ListenerEvent
     */
    public void notify(final ListenerEvent listenerType) {
        for (TListener ev : listeners) {
            try {
                ev.handle(listenerType);
            } catch (ClassCastException ignored) {

            }
        }
    }
}
