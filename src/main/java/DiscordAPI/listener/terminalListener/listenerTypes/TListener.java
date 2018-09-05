package DiscordAPI.listener.terminalListener.listenerTypes;

public interface TListener<T extends ListenerEvent> {
    void handle(T event);
}
