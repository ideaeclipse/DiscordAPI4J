package DiscordAPI.listener.dispatcher;

import DiscordAPI.listener.listenerTypes.ListenerEvent;

import java.util.ArrayList;
import java.util.List;


public class TDispatcher {
    private List<TListener<?>> listeners;

    public TDispatcher() {
        listeners = new ArrayList<>();
    }

    public <T extends ListenerEvent> void addListener(final TListener<T> listener) {
        listeners.add(listener);
    }

    public void notify(final ListenerEvent listenerType) {
        for (TListener ev : listeners) {
            try {
                ev.handle(listenerType);
            } catch (ClassCastException ignored) {

            }
        }
    }
}
