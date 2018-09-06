package DiscordAPI.listener.genericListener;

import DiscordAPI.utils.Async;

import java.util.ArrayList;
import java.util.List;

public class IDispatcher {
    private List<IListener<?, ?>> listeners;

    public IDispatcher() {
        listeners = new ArrayList<>();
    }

    public <K, T extends K> void addListener(IListener<K, T> listener) {
        listeners.add(listener);
    }

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
