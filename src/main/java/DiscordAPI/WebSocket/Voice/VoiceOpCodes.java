package DiscordAPI.WebSocket.Voice;

public enum VoiceOpCodes {
    Zero(0,true),
    Protocol(1,true),
    Ready(2,true),
    HeartBeat(3,true),
    Four(4,true),
    Speaking(5,true),
    HeartBeatACK(6,false),
    Seven(7,true),
    Initial(8,true);

    private int opcode;
    private boolean isSendable;
    VoiceOpCodes(int i, boolean b) {
        this.opcode = i;
        this.isSendable = b;
    }

    public int getOpcode() {
        return this.opcode;
    }

    public boolean isSendable() {
        return this.isSendable;
    }
}
