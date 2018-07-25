package DiscordAPI.listener.listenerTypes;

import DiscordAPI.Bot.BotImpl;

public abstract class ListenerEvent {
    private BotImpl botImpl;

    public ListenerEvent(BotImpl b) {
        this.botImpl = b;
    }

    public BotImpl getBotImpl(){
        return this.botImpl;
    }
}
