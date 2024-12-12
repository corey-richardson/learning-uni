package com.example.productuserssqlite;

public class Person {
    private int id;
    private String username;
    private String email;
    private String address;

    public Person(int id, String username, String email, String address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }
}
