package DiscordAPI.listener.listenerTypes;


import DiscordAPI.DiscordBot;

public abstract class ListenerEvent {
    private DiscordBot DiscordBot;

    public ListenerEvent(DiscordBot b) {
        this.DiscordBot = b;
    }

    public DiscordBot getDiscordBot(){
        return this.DiscordBot;
    }
}
