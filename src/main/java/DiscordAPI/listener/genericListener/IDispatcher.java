package DiscordAPI.listener.genericListener;

import java.util.ArrayList;
import java.util.List;

public class IDispatcher {
    private List<IListener<?, ?>> listeners;

    public IDispatcher() {
        listeners = new ArrayList<>();
    }

    public <K, T extends K> void addListener(IListener<K,T> listener) {
        listeners.add(listener);
    }

    public <K,T extends K> void notify(T listenerType) {
        for (IListener ev : listeners) {
            try {
                ev.handle(listenerType);
            } catch (ClassCastException ignored) {

            }
        }
    }
}
