package DiscordAPI.listener.listenerTypes;


import DiscordAPI.objects.DiscordBot;

public abstract class ListenerEvent {
    private DiscordBot DiscordBot;

    public ListenerEvent(final DiscordBot b) {
        this.DiscordBot = b;
    }

    public DiscordBot getDiscordBot(){
        return this.DiscordBot;
    }
}
