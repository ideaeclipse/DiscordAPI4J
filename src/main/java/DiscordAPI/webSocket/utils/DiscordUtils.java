package DiscordAPI.webSocket.utils;

import DiscordAPI.DiscordBot;
import DiscordAPI.objects.DChannel;
import DiscordAPI.objects.DRole;
import DiscordAPI.webSocket.jsonData.IJSONObject;
import DiscordAPI.webSocket.jsonData.identity.IDENTITY;
import DiscordAPI.webSocket.jsonData.Payloads;
import DiscordAPI.webSocket.utils.parsers.permissions.Permissions;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static DiscordAPI.webSocket.utils.DiscordUtils.DefaultLinks.*;

public class DiscordUtils {
    public static class HttpRequests {
        private static HttpsURLConnection authenticate(HttpsURLConnection con) {
            con.setRequestProperty("Authorization", "Bot " + token);
            return con;
        }

        private static HttpsURLConnection initialize(URL url) throws IOException {
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con = authenticate(con);
            return con;
        }

        public static Object get(String url) {
            try {
                HttpsURLConnection con = initialize(new URL(APIBASE + url));
                return printOutput(con.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void post(String url) {
            try {
                HttpsURLConnection con = initialize(new URL(APIBASE + url));
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setFixedLengthStreamingMode(0);
                printOutput(con.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void sendJson(String url, JSONObject object) {
            try {
                HttpsURLConnection con = initialize(new URL(APIBASE + url));
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Accept", "application/json");
                OutputStream os = con.getOutputStream();
                os.write(String.valueOf(object).getBytes("UTF-8"));
                os.close();
                printOutput(con.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static Object printOutput(InputStream inputStream) throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String string;
            StringBuffer response = new StringBuffer();
            while ((string = in.readLine()) != null) {
                response.append(string);
            }
            in.close();
            return DiscordUtils.convertToJSONOBJECT(response.toString());
        }
    }

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
            T o = getObject(c);
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
                    } else if (f.getType().equals(Integer.class)) {
                        f.set(o, Integer.parseInt(value));

                    } else if (f.getType().equals(Float.class)) {
                        f.set(o, Float.parseFloat(value));
                    } else if (f.getType().equals(Long.class)) {
                        f.set(o, Long.parseUnsignedLong(value));
                    } else if (f.getType().equals(Boolean.class)) {
                        f.set(o, Boolean.parseBoolean(value));
                    } else if (f.getType().isEnum()) {
                        f.set(o, f.getType().getEnumConstants()[Integer.parseInt(value)]);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return o;
        }

        private static <T> T getObject(Class<?> c) {
            T o = null;
            try {
                if (c.getName().contains("$")) {
                    Class<?> a = Payloads.class;
                    Object superC = a.getConstructor().newInstance();
                    o = (T) c.getConstructor(superC.getClass()).newInstance(superC);
                } else {
                    o = (T) c.getConstructor().newInstance();
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
        public static JSONObject BuildJSON(IJSONObject[] values) {
            JSONObject object = new JSONObject();
            for (IJSONObject d : values) {
                if (d == IDENTITY.token) {
                    object.put(d, token);
                } else if (!d.getaClass().isEnum()) {
                    object.put(d, d.getDefaultValue());
                } else if (d.getaClass().isEnum()) {
                    object.put(d, BuildJSON((IJSONObject[]) d.getaClass().getEnumConstants()));
                }
            }
            return object;
        }
    }
    public static class PermissionId{
        public static List<String> convertPermissions(Long p){
            List<String> perms = new ArrayList<>();
            System.out.println(p);
            for(Permissions permissions: Permissions.values()){
                System.out.print(permissions.name() + " " + p%permissions.getPermissionValue());
                if ((p % permissions.getPermissionValue() == 0)) {
                    System.out.print(" yes");
                    perms.add(permissions.name());
                }
                System.out.print("\n");
            }
            return perms;
        }
    }
    public static class DefaultLinks {
        public static String token;
        public static final String APIBASE = "https://discordapp.com/api/v6/";
        public static final String WEBSOCKET = "wss://gateway.discord.gg/?v=6&encoding=json";
        public static final String USER = "users/";
        public static final String GUILD = "guilds/";
        public static final String CHANNEL = "channels";
        public static final String MEMBER = "/members";
        public static final String ROLE = "/roles";
        public static final String USERME = USER + "@me";
    }
}
