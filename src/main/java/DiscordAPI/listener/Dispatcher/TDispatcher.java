package DiscordAPI.listener.Dispatcher;


import DiscordAPI.Bot.BotImpl;
import DiscordAPI.listener.listenerTypes.ListenerEvent;

import java.util.ArrayList;
import java.util.List;


public class TDispatcher {
    private List<TListener<?>> listeners;
    private BotImpl botImpl;
    public TDispatcher(BotImpl botImpl) {
        this.botImpl = botImpl;
        listeners = new ArrayList<>();
    }

    public <T extends ListenerEvent> void addListener(TListener<T> listener) {
        listeners.add(listener);
    }

    public <T extends ListenerEvent> void notify(T listenerType) {
        for (TListener ev : listeners) {
            try {
                ev.handle(listenerType);
            } catch (ClassCastException ignored) {

            }
        }
    }
}
