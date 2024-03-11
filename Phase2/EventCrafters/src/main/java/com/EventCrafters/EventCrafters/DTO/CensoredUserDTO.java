package com.EventCrafters.EventCrafters.DTO;

import com.EventCrafters.EventCrafters.model.User;

import java.util.List;

public class CensoredUserDTO {
    private Long id;
    private String name;
    private String username;
    private String photo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.photo = "/api/users/img/" + id;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public CensoredUserDTO(String name, String username, Long id) {
        this.name = name;
        this.username = username;
        this.id = id;
        this.photo = "/api/users/img/" + id;
    }

    public CensoredUserDTO(User user) {
        this.name = user.getName();
        this.username = user.getUsername();
        this.id = user.getId();
        this.photo = "/api/users/img/" + user.getId();
    }
}
