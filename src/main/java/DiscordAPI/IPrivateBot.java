package DiscordAPI;

import DiscordAPI.objects.AudioManager;
import DiscordAPI.objects.Interfaces.IChannel;
import DiscordAPI.objects.Interfaces.IRole;
import DiscordAPI.objects.Interfaces.IUser;
import DiscordAPI.utils.Async;

import java.util.List;

public interface IPrivateBot {
    Async.IU<List<IRole>> updateRoles();

    Async.IU<List<IUser>> updateUsers();

    Async.IU<List<IChannel>> updateChannels();

    AudioManager getAudioManager();

    List<IChannel> getChannels();

    List<IRole> getRoles();

    List<IUser> getUsers();

    long getGuildId();

    IDiscordBot getPublicBot();
}
