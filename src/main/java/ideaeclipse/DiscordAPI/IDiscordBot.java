package ideaeclipse.DiscordAPI;

import ideaeclipse.DiscordAPI.objects.*;
import ideaeclipse.DiscordAPI.objects.Interfaces.IChannel;
import ideaeclipse.DiscordAPI.objects.IDiscordUser;
import ideaeclipse.DiscordAPI.objects.Interfaces.IRole;
import ideaeclipse.DiscordAPI.objects.Interfaces.IUser;
import ideaeclipse.CustomProperties.Properties;
import ideaeclipse.reflectionListener.EventManager;

import java.util.List;

public interface IDiscordBot {
    IDiscordBot login();

    long getGuildId();

    //void updateChannels();

    List<IChannel> getChannels();

    List<IUser> getUsers();

    List<IRole> getRoles();

    EventManager getDispatcher();

    //returns bot
    IDiscordUser getBotUser();

    IChannel createDmChannel(IUser user);

    void setStatus(Payloads.GameTypes gameType, String gameName);

    Properties getProperties();
}
