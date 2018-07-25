package DiscordAPI.HttpApi;

import DiscordAPI.Bot.BotImpl;
import DiscordAPI.WebSocket.DefaultLinks;
import DiscordAPI.WebSocket.Utils.ConvertJSON;
import org.json.simple.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class HttpRequests {
    private BotImpl botImpl;

    public HttpRequests(BotImpl botImpl) {
        this.botImpl = botImpl;
    }

    private HttpsURLConnection authenticate(HttpsURLConnection con) {
        con.setRequestProperty("Authorization", "Bot " + botImpl.getToken());
        return con;
    }

    private HttpsURLConnection initialize(URL url) throws IOException {
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con = authenticate(con);
        return con;
    }

    public Object get(String url) {
        try {
            HttpsURLConnection con = initialize(new URL(DefaultLinks.APIBASE + url));
            return printOutput(con.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void post(String url) {
        try {
            HttpsURLConnection con = initialize(new URL(DefaultLinks.APIBASE + url));
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setFixedLengthStreamingMode(0);
            printOutput(con.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendJson(String url, JSONObject object) {
        try {
            HttpsURLConnection con = initialize(new URL(DefaultLinks.APIBASE + url));
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

    private Object printOutput(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String string;
        StringBuffer response = new StringBuffer();
        while ((string = in.readLine()) != null) {
            response.append(string);
        }
        in.close();
        return ConvertJSON.convertToJSONOBJECT(response.toString());
    }
}
