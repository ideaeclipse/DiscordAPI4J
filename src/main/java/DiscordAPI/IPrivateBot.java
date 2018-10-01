package DiscordAPI;

import DiscordAPI.objects.AudioManager;
import DiscordAPI.objects.Interfaces.IChannel;
import DiscordAPI.objects.Interfaces.IRole;
import DiscordAPI.objects.Interfaces.IUser;
import ideaeclipse.CustomProperties.Properties;
import ideaeclipse.JsonUtilities.Json;
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
}
