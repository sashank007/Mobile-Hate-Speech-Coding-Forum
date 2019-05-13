package com.example.codingforum;

public class User {

    public static String username;
    public static String password;
    public static String email;

    public User(String username , String password,  String email)
    {
        this.username=username;
        this.password=password;
        this.email=email;
    }
    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        User.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        User.password = password;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }
}
