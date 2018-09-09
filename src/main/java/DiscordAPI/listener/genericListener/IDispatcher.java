package DiscordAPI.listener.genericListener;

import DiscordAPI.utils.Async;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a generic dispatcher for any listener event/extended event
 *
 * @author ideaeclipse
 */
public class IDispatcher {
    private List<IListener<?, ?>> listeners;

    /**
     * constructor initializes the array
     */
    public IDispatcher() {
        listeners = new ArrayList<>();
    }

    /**
     * adds a listener to the array
     *
     * @param listener listener event
     * @param <K>      base event
     * @param <T>      extend event
     */
    public <K, T extends K> void addListener(IListener<K, T> listener) {
        listeners.add(listener);
    }

    /**
     * notifies all types of a listener
     *
     * @param listenerType listenerType
     * @param <K>          base type
     * @param <T>          extended type
     */
    public <K, T extends K> void notify(T listenerType) {
        Async.queue(() -> {
            for (IListener ev : listeners) {
                try {
                    ev.handle(listenerType);
                } catch (ClassCastException ignored) {

                }
            }
            return null;
        }, "Listener-" + listenerType.getClass().getSimpleName());
    }
}
