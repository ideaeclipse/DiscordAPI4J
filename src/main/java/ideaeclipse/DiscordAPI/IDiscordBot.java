package ideaeclipse.DiscordAPI;

import ideaeclipse.DiscordAPI.objects.*;
import ideaeclipse.DiscordAPI.objects.Interfaces.IChannel;
import ideaeclipse.DiscordAPI.objects.IDiscordUser;
import ideaeclipse.DiscordAPI.objects.Interfaces.IRole;
import ideaeclipse.DiscordAPI.objects.Interfaces.IUser;
import ideaeclipse.CustomProperties.Properties;
import ideaeclipse.reflectionListener.EventManager;

import java.util.List;

public interface IDiscordBot extends IPrivateBot {
    IDiscordBot login();

    //returns bot
    IDiscordUser getBotUser();

    IChannel createDmChannel(IUser user);

    void setStatus(Payloads.GameTypes gameType, String gameName);
}
