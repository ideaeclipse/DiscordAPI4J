package DiscordAPI.listener.listenerTypes;


import DiscordAPI.IDiscordBot;

public abstract class ListenerEvent {
    private IDiscordBot DiscordBot;

    public ListenerEvent(final IDiscordBot b) {
        this.DiscordBot = b;
    }

    public IDiscordBot getDiscordBot(){
        return this.DiscordBot;
    }
}
