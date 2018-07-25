package DiscordAPI.WebSocket.JsonData.Identity.Properties;

import DiscordAPI.WebSocket.JsonData.IJSONObject;

public enum PROPERTIES implements IJSONObject {
    $os(String.class, System.getProperty("os.name")),
    $browser(String.class, "D4J"),
    $device(String.class, "D4J");

    private Class<?> aClass;
    private Object o;

    PROPERTIES(Class<?> stringClass, Object o) {
        this.aClass = stringClass;
        this.o = o;
    }
    @Override
    public Object getDefaultValue() {
        return this.o;
    }
    @Override
    public Class<?> getaClass() {
        return aClass;
    }
}
