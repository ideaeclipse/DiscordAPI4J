package ideaeclipse.DiscordAPI;

import ideaeclipse.DiscordAPI.objects.AudioManager;
import ideaeclipse.DiscordAPI.objects.Interfaces.IChannel;
import ideaeclipse.DiscordAPI.objects.Interfaces.IRole;
import ideaeclipse.DiscordAPI.objects.Interfaces.IUser;
import ideaeclipse.CustomProperties.Properties;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.customLogger.LoggerManager;
import ideaeclipse.reflectionListener.EventManager;

import java.util.List;

public interface IPrivateBot {
    void updateRoles();

    void updateUsers();

    void updateChannels();

    AudioManager getAudioManager();

    List<IChannel> getChannels();

    List<IRole> getRoles();

    List<IUser> getUsers();

    long getGuildId();

    IDiscordBot getPublicBot();

    EventManager getDispatcher();

    Json getIdentity();

    Properties getProperties();

    LoggerManager getLoggerManager();
}
