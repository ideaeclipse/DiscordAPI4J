package DiscordAPI.objects.Interfaces;

import DiscordAPI.objects.Payloads;

public interface IGame {
    String getName();

    String getDetails();

    Payloads.GameTypes getType();

    String getState();
}
