package DiscordAPI.WebSocket.JsonData.Identity.Presence;

import DiscordAPI.WebSocket.JsonData.IJSONObject;
import org.json.simple.JSONObject;

public enum PRESENCE implements IJSONObject {
    //GAME.class
    game(GAME.class, null),
    status(String.class, "online"),
    since(long.class, null),
    afk(Boolean.class, false);

    private Class<?> aClass;
    private Object o;

    PRESENCE(Class<?> gameClass, Object o) {
        this.o = o;
        this.aClass = gameClass;
    }

    @Override
    public Class<?> getaClass() {
        return aClass;
    }

    @Override
    public Object getDefaultValue() {
        return o;
    }
}
