package DiscordAPI.listener.dispatcher;


import DiscordAPI.listener.listenerTypes.ListenerEvent;

public interface TListener<T extends ListenerEvent> {
    void handle(T event);
}
