package DiscordAPI.WebSocket.Voice.Ready;

import DiscordAPI.WebSocket.JsonData.IJSONObject;

public enum DATA implements IJSONObject {
    address(String.class,null),
    port(Integer.class,null),
    mode(String.class,"xsalsa20_poly1305");
    private Class<?> aClass;
    private Object defaultValue;

    DATA(Class<?> aClass, Object defaultValue) {
        this.aClass = aClass;
        this.defaultValue = defaultValue;

    }

    @Override
    public Class<?> getaClass() {
        return this.aClass;
    }

    @Override
    public Object getDefaultValue() {
        return this.defaultValue;
    }
}
