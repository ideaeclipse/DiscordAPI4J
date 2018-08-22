package DiscordAPI.objects;

public class VServerUpdate {
    private final String token;
    private final String endpoint;

    VServerUpdate(final String token, final String endpoint) {
        this.token = token;
        this.endpoint = endpoint;
    }

    public String getToken() {
        return token;
    }

    public String getEndpoint() {
        return endpoint;
    }

    @Override
    public String toString() {
        return "{Voice Server Update} Token: " + token + " EndPoint: " + endpoint;
    }
}
