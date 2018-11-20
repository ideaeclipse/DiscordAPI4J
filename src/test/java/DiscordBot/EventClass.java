package DiscordBot;

import ideaeclipse.DiscordAPI.listener.discordApiListener.Message_Create;
import ideaeclipse.DiscordAPI.listener.discordApiListener.Message_Update;
import ideaeclipse.reflectionListener.EventHandler;
import ideaeclipse.reflectionListener.Listener;

public class EventClass implements Listener {
    @EventHandler
    public void event2(Message_Update create) {
        //System.out.println("This bitch is an updated content message: " + create.getMessage().getContent());
    }
}
