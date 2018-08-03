package DiscordAPI.HttpApi;

import DiscordAPI.DiscordBot;
import DiscordAPI.WebSocket.Utils.DiscordUtils;
import org.json.simple.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class HttpRequests {
    private DiscordBot DiscordBot;

    public HttpRequests(DiscordBot DiscordBot) {
        this.DiscordBot = DiscordBot;
    }

    private HttpsURLConnection authenticate(HttpsURLConnection con) {
        con.setRequestProperty("Authorization", "Bot " + DiscordBot.getToken());
        return con;
    }

    private HttpsURLConnection initialize(URL url) throws IOException {
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con = authenticate(con);
        return con;
    }

    public Object get(String url) {
        try {
            HttpsURLConnection con = initialize(new URL(DiscordUtils.DefaultLinks.APIBASE + url));
            return printOutput(con.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void post(String url) {
        try {
            HttpsURLConnection con = initialize(new URL(DiscordUtils.DefaultLinks.APIBASE + url));
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
            HttpsURLConnection con = initialize(new URL(DiscordUtils.DefaultLinks.APIBASE + url));
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
        return DiscordUtils.convertToJSONOBJECT(response.toString());
    }
}
