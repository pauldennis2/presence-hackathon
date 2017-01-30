package com.tiy.webapp.requestBody;

/**
 * Created by Paul Dennis on 1/25/2017.
 */
public class UserLoginRequest {

    String email;
    String password;

    public UserLoginRequest () {

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
}
