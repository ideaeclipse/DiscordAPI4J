package ideaeclipse.DiscordAPI.utils;

import ideaeclipse.DiscordAPI.bot.DiscordBot;
import ideaeclipse.DiscordAPI.utils.annotations.JsonValidity;
import ideaeclipse.DiscordAPI.utils.interfaces.IHttpRequests;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.ListenerManager;
import ideaeclipse.reflectionListener.parents.Event;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Util for discord bot library
 *
 * @author Ideaeclipse
 */
public final class Util {
    /**
     * <p>
     * Executes a method and returns a checkResponse
     *
     * @param object     instance of a class from {@link Constructor#newInstance(Object...)}
     * @param methodName method name
     * @param json       json string to pass
     * @param <T>        instance type
     * @return checkResponse
     */
    public static <T extends Event> CheckResponse<?> check(final T object, final String methodName, final Json json) {
        try {
            Method initialize = object.getClass().getDeclaredMethod(methodName, Json.class);
            initialize.setAccessible(true);
            if (initialize.getParameterCount() == 1) {
                List<Annotation> validityList = Arrays.stream(initialize.getParameterAnnotations()[0]).filter(o -> o.annotationType().equals(JsonValidity.class)).collect(Collectors.toList());
                if (!validityList.isEmpty()) {
                    JsonValidity validity = (JsonValidity) validityList.get(0);
                    if (json.getMap().keySet().containsAll(Arrays.asList(validity.value()))) {
                        return new CheckResponse<>(CheckResponeType.EXECUTED, initialize.invoke(object, json));
                    }
                    return new CheckResponse<>(CheckResponeType.NOTEXECUTED, null);
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return new CheckResponse<>(CheckResponeType.NOTFOUND, null);
    }

    /**
     * Executes a constructor and callEvent with the return object {@link CheckResponse#getObject()}
     *
     * @param manager event manager {@link DiscordBot#getListenerManager()}
     * @param object  class to execute
     * @param json    json to pass
     * @param bot     discord bot instance
     * @param <T>     class type ensures it extends event
     * @return new checkResponse
     */
    public static <T extends Event> CheckResponse<T> checkConstructor(final ListenerManager manager, final Class<T> object, final Json json, final DiscordBot bot) {
        CheckResponse<T> r = checkConstructor(object, json, bot);
        if (r.getType().equals(CheckResponeType.EXECUTED))
            manager.callExecutables(r.getObject());
        return r;
    }

    /**
     * Executes a constructor if the params are valid in the json string
     *
     * @param object class to execute constructor
     * @param json   json to pass
     * @param bot    discord bot instance
     * @param <T>    class type ensures it extends event
     * @return new checkResponse
     */
    public static <T extends Event> CheckResponse<T> checkConstructor(final Class<T> object, final Json json, final DiscordBot bot) {
        try {
            Constructor<T> constructor = object.getDeclaredConstructor(Json.class, DiscordBot.class);
            if (constructor != null) {
                constructor.setAccessible(true);
                List<Annotation> validityList = Arrays.stream(constructor.getParameterAnnotations()[0]).filter(o -> o.annotationType().equals(JsonValidity.class)).collect(Collectors.toList());
                if (!validityList.isEmpty()) {
                    JsonValidity validity = (JsonValidity) validityList.get(0);
                    if (json.getMap().keySet().containsAll(Arrays.asList(validity.value()))) {
                        return new CheckResponse<>(CheckResponeType.EXECUTED, constructor.newInstance(json, bot));
                    }
                    return new CheckResponse<>(CheckResponeType.NOTEXECUTED, null);
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return new CheckResponse<>(CheckResponeType.NOTFOUND, null);
    }

    /**
     * Where all http requests go through
     *
     * @author Ideaeclipse
     * @see ideaeclipse.DiscordAPI.webSocket.rateLimit.RateLimitRecorder
     */
    public static class HttpRequests implements IHttpRequests {
        private final String APIBASE = "https://discordapp.com/api/v6/", token;

        public HttpRequests(final String token) {
            this.token = token;
        }

        /**
         * @param con Con url {@link #initialize(URL)}
         * @return returns connection with proper header
         */
        private HttpsURLConnection authenticate(HttpsURLConnection con) {
            con.setRequestProperty("Authorization", "Bot " + token);
            return con;
        }

        /**
         * @param url url string
         * @return returns a connection
         * @throws IOException if connection cannot be opened it throws an exception
         */
        private HttpsURLConnection initialize(final URL url) throws IOException {
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            return authenticate(con);
        }

        /**
         * If you need to use the Get Method for a web url
         *
         * @param url url string
         * @return Json String from the web api
         */
        public Object get(final String url) throws IOException {
            HttpsURLConnection con = initialize(new URL(APIBASE + url));
            return printOutput(con.getInputStream());
        }

        /**
         * If you need to use the Post Method for a web url
         *
         * @param url url string
         */
        public void post(final String url) throws IOException {
            HttpsURLConnection con = initialize(new URL(APIBASE + url));
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setFixedLengthStreamingMode(0);
            printOutput(con.getInputStream());
        }

        /**
         * If you need to use the Post Method for a web url
         *
         * @param url url string
         */
        public void put(final String url) throws IOException {
            HttpsURLConnection con = initialize(new URL(APIBASE + url));
            con.setDoOutput(true);
            con.setRequestMethod("PUT");
            con.setFixedLengthStreamingMode(0);
            printOutput(con.getInputStream());
        }

        public void delete(final String url) throws IOException {
            HttpsURLConnection con = initialize(new URL(APIBASE + url));
            con.setDoOutput(true);
            con.setRequestMethod("DELETE");
            con.setFixedLengthStreamingMode(0);
            printOutput(con.getInputStream());
        }

        /**
         * This method will send Post method with a json String
         *
         * @param url    url String
         * @param object Json Object
         * @return Returns the response from the WebServer
         */
        public Object sendJson(final String url, final Json object) throws IOException {
            HttpsURLConnection con = initialize(new URL(APIBASE + url));
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            OutputStream os = con.getOutputStream();
            os.write(object.toString().getBytes(StandardCharsets.UTF_8));
            os.close();
            return printOutput(con.getInputStream());
        }

        public Object sendFile(final String url, final String fileName) throws IOException {
            File file = new File(fileName);
            if (file.exists()) {
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
        private Object printOutput(final InputStream inputStream) throws IOException {
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
}
