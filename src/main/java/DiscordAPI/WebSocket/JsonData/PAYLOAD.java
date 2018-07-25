package DiscordAPI.WebSocket.JsonData;

import org.json.simple.JSONObject;

public enum PAYLOAD implements IJSONObject{
    op(Integer.class,null),
    d(JSONObject.class,null),
    s(Integer.class,null),
    t(String.class,null);

    private Class<?> aClass;
    private Object o;
    PAYLOAD(Class<?> integerClass,Object o) {
        this.aClass = integerClass;
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
