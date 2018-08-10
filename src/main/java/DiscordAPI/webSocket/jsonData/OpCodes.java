package DiscordAPI.webSocket.jsonData;

public enum OpCodes {
    Dispatch(0,false),
    Heartbeat(1,true),
    Identify(2,true),
    Status_Update(3,true),
    Voice_State_Update(4,true),
    Voice_Server_Ping(5,true),
    Resume(6,true),
    Reconnect(7,false),
    Request_Guild_Members(8,true),
    Invalid_Session(9,false),
    Hello(10,false),
    HeartBeat_ACK(11,false);

    private int opcode;
    private boolean isSendable;
    OpCodes(int i, boolean b) {
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
