package DiscordAPI.Objects.Audio;

import DiscordAPI.WebSocket.Utils.PayloadObject;
import DiscordAPI.WebSocket.Voice.VoiceOpCodes;
import com.neovisionaries.ws.client.WebSocket;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Arrays;

public class VoiceUDPSocket {
    private WebSocket webSocket;
    private volatile DatagramSocket udpSocket;
    private volatile InetSocketAddress address;
    private volatile int ssrc;
    private volatile byte[] secret;
    private volatile char sequence = 0;
    private volatile int timestamp = 0;

    public VoiceUDPSocket(WebSocket webSocket, String endpoint, int port, int ssrc) {
        try {
            this.webSocket = webSocket;
            this.udpSocket = new DatagramSocket();
            this.address = new InetSocketAddress(endpoint, port);
            this.ssrc = ssrc;
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public byte[] play() throws FileNotFoundException {
        File file = new File("C:\\Users\\Myles\\Desktop\\choose.mp3");
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(bytes)) != -1; ) {
                bos.write(bytes, 0, readNum); //no doubt here is 0
                //System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            System.out.println("error");
        }
        return bos.toByteArray();
    }

    public void send(byte[] audio) {

            OpusPacket packet = new OpusPacket(sequence, timestamp, ssrc, audio);
            packet.encrypt(secret);
            byte[] toSend = packet.asByteArray();
            System.out.println(Arrays.toString(toSend));
        try {
            udpSocket.send(new DatagramPacket(toSend, toSend.length, address));
            setSpeaking();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setSpeaking() {
        JSONObject object = new JSONObject();
        object.put("speaking", true);
        object.put("delay", 0);
        object.put("ssrc", ssrc);
        System.out.println(object);
        PayloadObject payloadObject = new PayloadObject(VoiceOpCodes.Speaking, object);
        System.out.println("**" + String.valueOf(payloadObject.getPayload()));
        webSocket.sendText(String.valueOf(payloadObject.getPayload()));
    }

    public void setSecret(byte[] secret) {
        System.out.println(Arrays.toString(secret));
        this.secret = secret;
    }
}
