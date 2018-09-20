package DiscordBot;

import DiscordAPI.listener.discordApiListener.listenerEvents.Message_Create;
import ideaeclipse.reflectionListener.EventHandler;
import ideaeclipse.reflectionListener.Listener;

public class EventClass implements Listener {
    @EventHandler
    public void event1(Message_Create create) {
        System.out.println(create.getMessage().getContent());
    }
}
