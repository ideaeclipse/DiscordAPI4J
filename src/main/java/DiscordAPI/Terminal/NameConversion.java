package DiscordAPI.Terminal;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class NameConversion {

    public static ArrayList<String> convert(Class<?>[] m) {
        ArrayList<String> data = new ArrayList<>();
        for (Class<?> c : m) {
            data.add(c.getSimpleName());
        }
        return data;
    }
}
