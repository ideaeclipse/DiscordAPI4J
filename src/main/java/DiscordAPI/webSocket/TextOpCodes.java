package DiscordAPI.webSocket;

public enum TextOpCodes {
    Dispatch,
    Heartbeat,
    Identify,
    Status_Update,
    Voice_State_Update,
    Voice_Server_Ping,
    Resume,
    Reconnect,
    Request_Guild_Members,
    Invalid_Session,
    Hello,
    HeartBeat_ACK
}
