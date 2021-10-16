package com.example.usersalbums.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Users {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Posts> posts;


    public Users(String username, String email, String password, List<Posts> posts) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.posts = posts;
    }

    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Users() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public List<Posts> getPosts() {
        return posts;
    }

    public void setPosts(List<Posts> posts) {
        this.posts = posts;
    }
}
