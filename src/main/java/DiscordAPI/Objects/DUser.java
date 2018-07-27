package DiscordAPI.Objects;

import java.util.ArrayList;
import java.util.List;

public class DUser {
    private Long id;
    private String name;
    private Integer discriminator;
    private List<DRole> roles;


    public DUser(Long id, String Name, Integer discriminator,List<DRole> roles) {
        this.id = id;
        this.name = Name;
        this.discriminator = discriminator;
        this.roles = roles;
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

    public List<DRole> getRoles() {
        return roles;
    }
}
