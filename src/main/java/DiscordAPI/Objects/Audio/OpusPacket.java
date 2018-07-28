package DiscordAPI.Objects.Audio;

import crypto.TweetNaclFast;

import org.apache.commons.lang3.ArrayUtils;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class OpusPacket {

    final RTPHeader header;

    private byte[] audio;


    private boolean isEncrypted = false;

    OpusPacket(DatagramPacket udpPacket) {
        this.header = new RTPHeader(Arrays.copyOf(udpPacket.getData(), RTPHeader.LENGTH));
        this.audio = Arrays.copyOfRange(udpPacket.getData(), RTPHeader.LENGTH, udpPacket.getLength());
        this.isEncrypted = true;
    }

    OpusPacket(char sequence, int timestamp, int ssrc, byte[] audio) {
        this(new RTPHeader(sequence, timestamp, ssrc), audio);
    }

    private OpusPacket(RTPHeader header, byte[] audio) {
        this.header = header;
        this.audio = audio;
    }

    void encrypt(byte[] secret) {
        if (isEncrypted) throw new IllegalStateException("Attempt to encrypt already-encrypted audio packet.");
        audio = new TweetNaclFast.SecretBox(secret).box(audio, getNonce());
        isEncrypted = true;
    }

    void decrypt(byte[] secret) {
        if (!isEncrypted) throw new IllegalStateException("Attempt to decrypt unencrypted audio packet.");
        audio = new TweetNaclFast.SecretBox(secret).open(audio, getNonce());
        isEncrypted = false;

        if (header.type == (byte) 0x90 && audio[0] == (byte) 0xBE && audio[1] == (byte) 0xDE) {
            int hlen = audio[2] << 8 | audio[3];
            int i = 4;
            for (; i < hlen + 4; i++)
            {
                int b = audio[i];
                int len = (b & 0x0F) + 1;
                i += len;
            }
            while (audio[i] == 0)
                i++;

            byte[] buf = new byte[audio.length - i];
            System.arraycopy(audio, i, buf, 0, buf.length);
            audio = buf;
        }
    }

    byte[] asByteArray() {
        return ArrayUtils.addAll(header.asByteArray(), audio);
    }

    byte[] getAudio() {
        return ArrayUtils.clone(audio);
    }

    private byte[] getNonce() {
        return ArrayUtils.addAll(header.asByteArray(), new byte[12]);
    }

    static class RTPHeader {

        static final int LENGTH = 12;

        final byte type;

        final byte version;

        final char sequence;

        final int timestamp;

        final int ssrc;

        RTPHeader(byte[] header) {
            ByteBuffer buf = ByteBuffer.wrap(header);
            this.type = buf.get(0);
            this.version = buf.get(1);
            this.sequence = buf.getChar(2);
            this.timestamp = buf.getInt(4);
            this.ssrc = buf.getInt(8);
        }

        RTPHeader(char sequence, int timestamp, int ssrc) {
            this.type = (byte) 0x80;
            this.version = 0x78;
            this.sequence = sequence;
            this.timestamp = timestamp;
            this.ssrc = ssrc;
        }

        byte[] asByteArray() {
            return ByteBuffer.allocate(LENGTH)
                    .put(0, type)
                    .put(1, version)
                    .putChar(2, sequence)
                    .putInt(4, timestamp)
                    .putInt(8, ssrc)
                    .array();
        }
    }
}
