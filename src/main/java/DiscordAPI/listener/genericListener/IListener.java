package DiscordAPI.listener.genericListener;

public interface IListener<K, T extends K> {
    void handle(T event);
}
