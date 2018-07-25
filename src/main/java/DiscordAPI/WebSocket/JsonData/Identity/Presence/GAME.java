package DiscordAPI.WebSocket.JsonData.Identity.Presence;

import DiscordAPI.WebSocket.JsonData.IJSONObject;

public enum GAME implements IJSONObject {
    name(String.class, "cm help for commands"),
    type(Integer.class, 0);

    private Class<?> aClass;
    private Object o;

    GAME(Class<?> integerClass, Object o) {
        this.aClass = integerClass;
        this.o = o;
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
