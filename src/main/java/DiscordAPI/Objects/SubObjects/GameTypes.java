package DiscordAPI.Objects.SubObjects;

public enum GameTypes {
    Playing(0),
    Streaming(1),
    Listening(2);

    private int type;

    GameTypes(int i) {
        this.type = i;
    }

    public int getType() {
        return type;
    }
}
