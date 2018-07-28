package DiscordAPI.WebSocket.Voice.Ready;

import DiscordAPI.WebSocket.JsonData.IJSONObject;
import org.json.simple.JSONObject;

public enum READY implements IJSONObject {
    protocol(String.class,"udp"),
    data(DATA.class,null);
    private Class<?> aClass;
    private Object defaultValue;

    READY(Class<?> aClass, Object defaultValue) {
        this.aClass = aClass;
        this.defaultValue = defaultValue;
    }

    @Override
    public Class<?> getaClass() {
        return aClass;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }
}
