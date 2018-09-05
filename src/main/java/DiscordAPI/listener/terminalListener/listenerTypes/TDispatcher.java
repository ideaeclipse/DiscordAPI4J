package DiscordAPI.listener.terminalListener.listenerTypes;

import DiscordAPI.Terminal.Terminal;
import DiscordAPI.utils.DiscordLogger;

import java.util.ArrayList;
import java.util.List;

public class TDispatcher {
    private static final DiscordLogger LOGGER = new DiscordLogger(TDispatcher.class.getName());
    private List<TListener<?>> listeners;
    private Terminal terminal;

    public TDispatcher(Terminal terminal) {
        this.terminal = terminal;
        listeners = new ArrayList<>();
    }

    public <T extends ListenerEvent> void addListener(TListener<T> listener) {
        listeners.add(listener);
    }

    public <T extends ListenerEvent> void notify(T listenerType) {
        LOGGER.info("Dispatcher notified with type: " + listenerType.getClass().getSimpleName());
        for (TListener ev : listeners) {
            try {
                ev.handle(listenerType);
            } catch (ClassCastException ignored) {

            }
        }
    }
}
