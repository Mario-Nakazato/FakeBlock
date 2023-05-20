package com.nak.fakeblock.models;

public class User {
    public String username;
    public String email;

    public User() {
        // Construtor padrão necessário para chamadas para DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
