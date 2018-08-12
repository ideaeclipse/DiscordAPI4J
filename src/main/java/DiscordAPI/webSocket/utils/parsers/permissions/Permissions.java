package DiscordAPI.webSocket.utils.parsers.permissions;

public enum Permissions {
    Administrator(0x8),
    ViewAuditLog(0x80),
    ManageServer(0x20),
    ManageRoles(0x10000000),
    ManageChannels(0x10),
    KickMembers(0x2),
    BanMembers(0x4),
    CreateInstantInvite(0x1),
    ChangeNickName(0x4000000),
    ManageNickNames(0x8000000),
    ManageEmojis(0x40000000),
    ManageWebHooks(0x20000000),
    ReadMessages(0x400),
    SendTTSMessages(0x1000),
    EmbededLinks(0x4000),
    ReadMessageHistory(0x10000),
    UseExternalEmojis(0x40000),
    SendMessages(0x800),
    ManageMessages(0x2000),
    AttachFiles(0x8000),
    MentionEveryone(0x20000),
    AddReactions(0x40),
    ViewChannel(0x400),
    Connect(0x100000),
    MuteMembers(0x400000),
    MoveMembers(0x1000000),
    Speak(0x200000),
    DeafenMembers(0x800000),
    UseVoiceActivity(0x2000000),
    PrioritySpeaking(0x100);

    private int permissionValue;
    Permissions(int i) {
        this.permissionValue = i;
    }

    public int getPermissionValue() {
        return permissionValue;
    }
}
