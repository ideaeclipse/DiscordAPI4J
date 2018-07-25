package DiscordAPI.Objects;

public class DUser {
    private Long id;
    private String name;
    private Integer discriminator;

    public DUser(Long id, String Name, Integer discriminator) {
        this.id = id;
        this.name = Name;
        this.discriminator = discriminator;
    }

    public String getName() {
        return name;
    }

    public Integer getDiscriminator() {
        return discriminator;
    }

    public Long getId() {
        return id;
    }
}
