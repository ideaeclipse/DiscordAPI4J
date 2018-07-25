package DiscordAPI.WebSocket.JsonData.Identity;

import DiscordAPI.WebSocket.JsonData.Identity.Presence.PRESENCE;
import DiscordAPI.WebSocket.JsonData.IJSONObject;
import DiscordAPI.WebSocket.JsonData.Identity.Properties.PROPERTIES;

import java.util.ArrayList;

public enum IDENTITY implements IJSONObject {
    token(String.class, null),
    //PROPERTIES.class
    properties(PROPERTIES.class, null),
    compress(Boolean.class, true),
    large_threshold(Integer.class, 250),
    shard(ArrayList.class, null),
    //PRESENCE.class
    presence(PRESENCE.class, null);

    private Class<?> aClass;
    private Object o;

    IDENTITY(Class<?> stringClass, Object o) {
        this.aClass = stringClass;
        this.o = o;
    }

    @Override
    public Class<?> getaClass() {
        return this.aClass;
    }

    @Override
    public Object getDefaultValue() {
        return this.o;
    }
}
