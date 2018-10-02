package ideaeclipse.DiscordAPI.objects.Interfaces;

import ideaeclipse.DiscordAPI.objects.Payloads;

public interface IGame {
    String getName();

    String getDetails();

    Payloads.GameTypes getType();

    String getState();
}
