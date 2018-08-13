package DiscordAPI.utils;

import DiscordAPI.objects.Channel;
import DiscordAPI.objects.Role;
import DiscordAPI.webSocket.jsonData.IJSONObject;
import DiscordAPI.webSocket.jsonData.identity.IDENTITY;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static DiscordAPI.utils.DiscordUtils.DefaultLinks.*;

public class DiscordUtils {
    public static class HttpRequests {
        private static HttpsURLConnection authenticate(HttpsURLConnection con) {
            con.setRequestProperty("Authorization", "Bot " + token);
            return con;
        }

        private static HttpsURLConnection initialize(final URL url) throws IOException {
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con = authenticate(con);
            return con;
        }

        public static Object get(final String url) {
            try {
                final HttpsURLConnection con = initialize(new URL(APIBASE + url));
                return printOutput(con.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void post(final String url) {
            try {
                final HttpsURLConnection con = initialize(new URL(APIBASE + url));
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setFixedLengthStreamingMode(0);
                printOutput(con.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void sendJson(final String url, final JSONObject object) {
            try {
                final HttpsURLConnection con = initialize(new URL(APIBASE + url));
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

        private static Object printOutput(final InputStream inputStream) throws IOException {
            final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String string;
            StringBuffer response = new StringBuffer();
            while ((string = in.readLine()) != null) {
                response.append(string);
            }
            in.close();
            return DiscordUtils.convertToJSONOBJECT(response.toString());
        }
    }

    public static Object convertToJSONOBJECT(final String message) {
        final JSONParser parser = new JSONParser();
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


    public static class Search {
        public static Channel CHANNEL(final List<Channel> channels, final String channelName) {
            for (Channel channel : channels) {
                if (channel.getName().toLowerCase().equals(channelName.toLowerCase())) {
                    return channel;
                }
            }
            return null;
        }

        public static Role ROLES(final List<Role> roles, final Long id) {
            for (Role role : roles) {
                if (role.getId().equals(id)) {
                    return role;
                }
            }
            return null;
        }
    }

    public static class BuildJSON {
        public static JSONObject BuildJSON(final IJSONObject[] values) {
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
