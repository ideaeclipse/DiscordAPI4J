package DiscordAPI.Objects.SubObjects;

public class DGame {
    private String name;
    private String state;
    private String details;
    private Integer type;

    public DGame(String name, String state,String details,Integer type){
        this.name = name;
        this.state = state;
        this.details = details;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public Integer getType() {
        return type;
    }

    public String getState() {
        return state;
    }
}
