package DiscordAPI.utils;

import DiscordAPI.objects.Channel;
import DiscordAPI.objects.Role;
import DiscordAPI.objects.User;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static DiscordAPI.utils.DiscordUtils.DefaultLinks.*;

public class DiscordUtils {
    static class HttpRequests {

        private static HttpsURLConnection authenticate(HttpsURLConnection con) {
            con.setRequestProperty("Authorization", "Bot " + token);
            return con;
        }

        private static HttpsURLConnection initialize(final URL url) throws IOException {
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con = authenticate(con);
            return con;
        }

        static Object get(final String url) {
            HttpsURLConnection con = null;
            try {
                con = initialize(new URL(APIBASE + url));
                return printOutput(con.getInputStream());
            } catch (IOException e) {
                try {
                    assert con != null;
                    throw new Exceptions.HttpResponseError(con.getResponseCode());
                } catch (Exceptions.HttpResponseError | IOException ignored) {

                }
            }
            return null;
        }

        static void post(final String url) {
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

        static Object sendJson(final String url, final Json object) {
            try {
                HttpsURLConnection con = initialize(new URL(APIBASE + url));
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Accept", "application/json");
                OutputStream os = con.getOutputStream();
                os.write(object.toString().getBytes("UTF-8"));
                os.close();
                return printOutput(con.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private static Object printOutput(final InputStream inputStream) throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String string;
            StringBuilder response = new StringBuilder();
            while ((string = in.readLine()) != null) {
                response.append(string);
            }
            in.close();
            //System.out.println("RESPONSE: " + response);
            return response.toString();
        }
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

        public static User USER(final List<User> users, final String userName) {
            for (User user : users) {
                if (user.getName().toLowerCase().equals(userName.toLowerCase())) {
                    return user;
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

    public static class PermissionId {
        public static List<String> convertPermissions(Long p) {
            List<String> perms = new ArrayList<>();
            System.out.println(p);
            for (Permissions permissions : Permissions.values()) {
                System.out.print(permissions.name() + " " + p % permissions.getPermissionValue());
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
        public static RateLimitRecorder rateLimitRecorder;

        static final String APIBASE = "https://discordapp.com/api/v6/";
        public static final String WEBSOCKET = "wss://gateway.discord.gg/?v=6&encoding=json";
        static final String USER = "users/";
        public static final String GUILD = "guilds/";
        public static final String CHANNEL = "channels";
        public static final String MEMBER = "/members";
        public static final String ROLE = "/roles";
        public static final String USERME = USER + "@me";

        static final String httpErrorFormat = "Discord Http API Responsed with error code: %code";
    }

    static class Exceptions {

        public static class HttpResponseError extends Exception implements IDiscordExceptions {
            private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));

            HttpResponseError(final Integer errorCode) {
                switch (errorCode) {
                    case 401:
                        try {
                            throw new AuthenticationError(errorCode);
                        } catch (AuthenticationError ignored) {

                        }
                        break;
                    default:
                        logger.error(getResponse());
                        break;
                }
            }

            @Override
            public String getResponse() {
                return httpErrorFormat.replace("%code", "unknown");
            }
        }

        public static class AuthenticationError extends Exception implements IDiscordExceptions {
            private final DiscordLogger logger = new DiscordLogger(String.valueOf(this.getClass()));
            private final Integer errorCode;

            AuthenticationError(final Integer errorCode) {
                this.errorCode = errorCode;
                logger.error(getResponse());
                System.exit(1);
            }

            @Override
            public String getResponse() {
                return httpErrorFormat.replace("%code", String.valueOf(errorCode)) + " try resetting your token";
            }
        }

        public interface IDiscordExceptions {
            String getResponse();
        }
    }
}
