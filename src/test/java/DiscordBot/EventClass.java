package DiscordBot;

import ideaeclipse.DiscordAPI.listener.discordApiListener.Message_Create;
import ideaeclipse.DiscordAPI.listener.discordApiListener.Message_Update;
import ideaeclipse.reflectionListener.EventHandler;
import ideaeclipse.reflectionListener.Listener;

public class EventClass implements Listener {
    @EventHandler
    public void event2(Message_Create create) {
        if(create.getMessage().getContent().equals("ping")){
            create.getMessage().getChannel().sendMessage("pong");
        }
    }
}
