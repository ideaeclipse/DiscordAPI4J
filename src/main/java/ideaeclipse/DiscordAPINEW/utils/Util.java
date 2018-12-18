package ideaeclipse.DiscordAPINEW.utils;

import ideaeclipse.DiscordAPINEW.utils.annotations.JsonValidity;
import ideaeclipse.DiscordAPINEW.utils.interfaces.IHttpRequests;
import ideaeclipse.DiscordAPINEW.webSocket.RateLimitRecorder;
import ideaeclipse.JsonUtilities.Json;
import ideaeclipse.reflectionListener.Event;
import ideaeclipse.reflectionListener.EventManager;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Util {
    public static final RateLimitRecorder rateLimitRecorder = new RateLimitRecorder();
    public static IHttpRequests requests;

    public static <T extends Event> boolean check(final EventManager manager, final T object, final String methodName, final Json json) {
        if (check(object, methodName, json))
            manager.callEvent(object);
        return true;
    }

    public static <T extends Event> boolean check(final T object, final Json json) {
        return check(object, "initialize", json);
    }

    public static <T extends Event> boolean check(final T object, final String methodName, final Json json) {
        try {
            Method initialize = object.getClass().getDeclaredMethod(methodName, Json.class);
            initialize.setAccessible(true);
            if (initialize.getParameterCount() == 1) {
                List<Annotation> validityList = Arrays.stream(initialize.getParameterAnnotations()[0]).filter(o -> o.annotationType().equals(JsonValidity.class)).collect(Collectors.toList());
                if (!validityList.isEmpty()) {
                    JsonValidity validity = (JsonValidity) validityList.get(0);
                    boolean returnStatus = json.getMap().keySet().containsAll(Arrays.asList(validity.value()));
                    if (returnStatus)
                        initialize.invoke(object, json);
                    return returnStatus;
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("ALL")
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
            con = authenticate(con);
            return con;
        }

        /**
         * If you need to use the Get Method for a web url
         *
         * @param url url string
         * @return Json String from the web api
         */
        public Object get(final String url) {
            HttpsURLConnection con = null;
            try {
                con = initialize(new URL(APIBASE + url));
                return printOutput(con.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * If you need to use the Post Method for a web url
         *
         * @param url url string
         */
        public void post(final String url) {
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
        public Object sendJson(final String url, final Json object) {
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

        public Object sendFile(final String url, final String fileName) {
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
