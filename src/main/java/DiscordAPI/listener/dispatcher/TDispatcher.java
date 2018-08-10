package DiscordAPI.listener.dispatcher;


import DiscordAPI.DiscordBot;
import DiscordAPI.listener.listenerTypes.ListenerEvent;

import java.util.ArrayList;
import java.util.List;


public class TDispatcher {
    private List<TListener<?>> listeners;
    private DiscordBot DiscordBot;
    public TDispatcher(DiscordBot DiscordBot) {
        this.DiscordBot = DiscordBot;
        listeners = new ArrayList<>();
    }

    public <T extends ListenerEvent> void addListener(TListener<T> listener) {
        listeners.add(listener);
    }

    public void notify(Object listenerType) {
        for (TListener ev : listeners) {
            try {
                ev.handle((ListenerEvent) listenerType);
            } catch (ClassCastException ignored) {

            }
        }
    }
}
