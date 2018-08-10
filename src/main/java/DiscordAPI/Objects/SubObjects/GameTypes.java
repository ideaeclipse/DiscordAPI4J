package DiscordAPI.Objects.SubObjects;

public enum GameTypes {
    Playing,
    Streaming,
    Listening;


    public static int getType(GameTypes g) {
        return g.ordinal();
    }
}
