package DiscordAPI.objects.subObjects;

public enum GameTypes {
    Playing,
    Streaming,
    Listening;


    public static int getType(GameTypes g) {
        return g.ordinal();
    }
}
