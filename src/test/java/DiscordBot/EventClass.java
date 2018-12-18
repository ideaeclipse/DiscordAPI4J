package DiscordBot;

import ideaeclipse.DiscordAPI.listener.discordApiListener.Message_Create;
import ideaeclipse.reflectionListener.Listener;
import ideaeclipse.reflectionListener.annotations.EventHandler;

public class EventClass implements Listener {
    @EventHandler
    public void event2(Message_Create create) {
        if(create.getMessage().getContent().equals("ping")){
            create.getMessage().getChannel().sendMessage("pong");
        }
    }
}
