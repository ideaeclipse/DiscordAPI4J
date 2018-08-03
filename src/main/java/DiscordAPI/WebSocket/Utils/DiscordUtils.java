package DiscordAPI.WebSocket.Utils;

import DiscordAPI.DiscordBot;
import DiscordAPI.Objects.DChannel;
import DiscordAPI.Objects.DRole;
import DiscordAPI.WebSocket.JsonData.IJSONObject;
import DiscordAPI.WebSocket.JsonData.Identity.IDENTITY;
import DiscordAPI.WebSocket.Utils.Parsers.Payloads;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DiscordUtils {
    public static Object convertToJSONOBJECT(String message) {
        JSONParser parser = new JSONParser();
        try {
            return parser.parse(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ThreadFactory createDaemonThreadFactory(String threadName) {
        return (runnable) -> {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            if (threadName != null)
                thread.setName(threadName);
            thread.setDaemon(true);
            return thread;
        };
    }

    public static class Parser {

        public static <T> T convertToJSON(JSONObject object, Class<?> c) {
            Object o = getObject(c);
            try {
                for (Object s : object.keySet()) {
                    Field f;
                    try {
                        f = c.getField(String.valueOf(s));
                    } catch (NoSuchFieldException ignored) {
                        continue;
                    }
                    String value = String.valueOf(object.get(s));
                    if (f.getType().equals(String.class)) {
                        f.set(o, value);
                    }
                    if (f.getType().equals(Integer.class)) {
                        f.set(o, Integer.parseInt(value));

                    }
                    if (f.getType().equals(Float.class)) {
                        f.set(o, Float.parseFloat(value));
                    }
                    if (f.getType().equals(Long.class)) {
                        f.set(o, Long.parseUnsignedLong(value));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return (T) o;
        }

        private static Object getObject(Class<?> c) {
            Object o = null;
            try {
                if (c.getName().contains("$")) {
                    Class<?> a = Payloads.class;
                    Object superC = a.getConstructor().newInstance();
                    o = c.getConstructor(superC.getClass()).newInstance(superC);
                } else {
                    o = c.getConstructor().newInstance();
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return o;
        }
    }

    public static class Search {
        public static DChannel CHANNEL(List<DChannel> channels, String channelName) {
            for (DChannel channel : channels) {
                if (channel.getName().toLowerCase().equals(channelName.toLowerCase())) {
                    return channel;
                }
            }
            return null;
        }

        public static DRole ROLES(List<DRole> roles, Long id) {
            for (DRole role : roles) {
                if (role.getId().equals(id)) {
                    return role;
                }
            }
            return null;
        }
    }

    public static class BuildJSON {
        public static JSONObject BuildJSON(IJSONObject[] values, DiscordBot DiscordBot) {
            JSONObject object = new JSONObject();
            for (IJSONObject d : values) {
                if (d == IDENTITY.token) {
                    object.put(d, DiscordBot.getToken());
                } else if (!d.getaClass().isEnum()) {
                    object.put(d, d.getDefaultValue());
                } else if (d.getaClass().isEnum()) {
                    object.put(d, BuildJSON((IJSONObject[]) d.getaClass().getEnumConstants(), DiscordBot));
                }
            }
            return object;
        }
    }

    public static class DefaultLinks {
        public static final String APIBASE = "https://discordapp.com/api/v6/";
        public static final String WEBSOCKET = "wss://gateway.discord.gg/?v=6&encoding=json";
        public static final String USERAPI = APIBASE + "users/";
        public static final String USERME = USERAPI + "@me";
    }
}
