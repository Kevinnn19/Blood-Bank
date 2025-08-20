package com.example.bloodbank.project.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;


@Entity
public class Authority 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Authority() {}

    public Authority(String authority, User user) 
    {
        this.authority = authority;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAuthority() { return authority; }
    public void setAuthority(String authority) { this.authority = authority; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
