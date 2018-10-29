package ideaeclipse.DiscordAPI.utils;

import ideaeclipse.DiscordAPI.IPrivateBot;
import ideaeclipse.DiscordAPI.objects.Interfaces.IChannel;
import ideaeclipse.DiscordAPI.objects.Interfaces.IRole;
import ideaeclipse.DiscordAPI.objects.Interfaces.IUser;
import ideaeclipse.DiscordAPI.objects.Payloads;
import ideaeclipse.JsonUtilities.Json;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

import static ideaeclipse.DiscordAPI.utils.DiscordUtils.DefaultLinks.*;


/**
 * This class is the utility class and has various function each described in their class header
 *
 * @author ideaeclipse
 */
public class DiscordUtils {
    /**
     * Anything related to Custom terminal
     *
     * @author ideaeclipse
     */

    public static class CustomTerminal {
        public static ArrayList<String> convert(Class<?>[] m) {
            ArrayList<String> data = new ArrayList<>();
            for (Class<?> c : m) {
                data.add(c.getSimpleName());
            }
            return data;
        }
    }

    /**
     * This class is used to Handle all Http Api Requests
     *
     * @author ideaeclipse
     */
    static class HttpRequests {
        /**
         * @param con Con url {@link #initialize(URL)}
         * @return returns connection with proper header
         */
        private static HttpsURLConnection authenticate(HttpsURLConnection con) {
            con.setRequestProperty("Authorization", "Bot " + token);
            return con;
        }

        /**
         * @param url url string
         * @return returns a connection
         * @throws IOException if connection cannot be opened it throws an exception
         */
        private static HttpsURLConnection initialize(final URL url) throws IOException {
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con = authenticate(con);
            return con;
        }

        /**
         * If you need to use the Get Method for a web url
         *
         * @param url url string
         * @return Json String from the web api
         */
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

        /**
         * If you need to use the Post Method for a web url
         *
         * @param url url string
         */
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

        /**
         * This method will send Post method with a json String
         *
         * @param url    url String
         * @param object Json Object
         * @return Returns the response from the WebServer
         */
        static Object sendJson(final String url, final Json object) {
            try {
                HttpsURLConnection con = initialize(new URL(APIBASE + url));
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Accept", "application/json");
                OutputStream os = con.getOutputStream();
                os.write(object.toString().getBytes(StandardCharsets.UTF_8));
                os.close();
                return printOutput(con.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        static Object sendFile(final String url, final String fileName) {
            File file = new File(fileName);
            if (file.exists()) {
                try {
                    String attachmentName = fileName.substring(0, fileName.indexOf('.'));
                    String attachmentFileName = file.getName();
                    String crlf = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    HttpsURLConnection con = initialize(new URL(APIBASE + url));
                    con.setDoOutput(true);
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Connection", "Keep-Alive");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-type", "multipart/form-data;boundary=" + boundary);
                    DataOutputStream request = new DataOutputStream(con.getOutputStream());
                    request.writeBytes(twoHyphens + boundary + crlf);
                    request.writeBytes("Content-Disposition: form-data; name=\"" + attachmentName + "\";filename=\"" + attachmentFileName + "\"" + crlf);
                    request.writeBytes(crlf);
                    request.write(Files.readAllBytes(file.toPath()));
                    request.writeBytes(crlf);
                    request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
                    request.flush();
                    request.close();
                    return printOutput(con.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        /**
         * This method returns an Object based on your request
         *
         * @param inputStream the input stream from the connection
         * @return Object from the webserver
         * @throws IOException throws exception
         */
        private static Object printOutput(final InputStream inputStream) throws IOException {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String string;
            StringBuilder response = new StringBuilder();
            while ((string = in.readLine()) != null) {
                response.append(string);
            }
            in.close();
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
        public static IChannel CHANNEL(final List<IChannel> channels, final String channelName) {
            List<IChannel> channelSet = channels.stream().filter(o -> o.getType().equals(Payloads.ChannelTypes.textChannel) && o.getName().toLowerCase().equals(channelName.toLowerCase())).collect(Collectors.toList());
            if (!channelSet.isEmpty())
                return channelSet.get(0);
            else
                return null;
        }

        public static IChannel VOICECHANNEL(final List<IChannel> channels, final String channelName) {
            for (IChannel channel : channels) {
                if (channel.getType().equals(Payloads.ChannelTypes.voiceChannel)) {
                    if (channel.getName().toLowerCase().equals(channelName.toLowerCase())) {
                        return channel;
                    }
                }
            }
            return null;
        }

        public static IUser USER(final List<IUser> users, final String userName) {
            for (IUser user : users) {
                if (user.getDiscordUser().getName().toLowerCase().equals(userName.toLowerCase())) {
                    return user;
                }
            }
            return null;
        }

        public static IUser USER(final List<IUser> users, final Long userId) {
            for (IUser user : users) {
                if (user.getDiscordUser().getId().equals(userId)) {
                    return user;
                }
            }
            return null;
        }

        public static IRole ROLES(final List<IRole> roles, final Long id) {
            for (IRole role : roles) {
                if (role.getId().equals(id)) {
                    return role;
                }
            }
            return null;
        }

        public static IRole ROLES(final List<IRole> roles, final String id) {
            for (IRole role : roles) {
                if (role.getName().toLowerCase().equals(id.toLowerCase())) {
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
        public static IPrivateBot bot;

        static final String APIBASE = "https://discordapp.com/api/v6/";
        public static final String WEBSOCKET = "wss://gateway.discord.gg/?v=6&encoding=json";
        static final String USER = "users/";
        public static final String GUILD = "guilds/";
        public static final String CHANNEL = "channels";
        public static final String MEMBER = "/members";
        public static final String ROLE = "/roles";
        public static final String USERME = USER + "@me";

        static final String httpErrorFormat = "Discord Http API Responded with error code: %code";
    }

    static class Exceptions {

        public static class HttpResponseError extends Exception implements IDiscordExceptions {

            HttpResponseError(final Integer errorCode) {
                switch (errorCode) {
                    case 401:
                        try {
                            throw new AuthenticationError(errorCode);
                        } catch (AuthenticationError ignored) {

                        }
                        break;
                }
            }

            @Override
            public String getResponse() {
                return httpErrorFormat.replace("%code", "unknown");
            }
        }

        public static class AuthenticationError extends Exception implements IDiscordExceptions {
            private final Integer errorCode;

            AuthenticationError(final Integer errorCode) {
                this.errorCode = errorCode;
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
