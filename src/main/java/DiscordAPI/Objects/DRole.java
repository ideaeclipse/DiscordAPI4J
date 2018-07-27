package DiscordAPI.Objects;

public class DRole {
    private Long id;
    private String name;
    private Integer colourCode;
    private Integer position;
    private Long permission;
    public DRole(Long id,String name,Integer colourCode,Integer position,Long permission){
        this.id = id;
        this.name = name;
        this.colourCode = colourCode;
        this.position = position;
        this.permission = permission;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getColourCode() {
        return colourCode;
    }

    public Integer getPosition() {
        return position;
    }

    public Long getPermission() {
        return permission;
    }
}
