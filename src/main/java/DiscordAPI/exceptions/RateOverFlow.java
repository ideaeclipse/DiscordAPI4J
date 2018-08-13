package DiscordAPI.exceptions;

public class RateOverFlow extends Exception {
    public RateOverFlow() {
        super();
    }

    public String getResponse() {
        return "yes";
    }
}
