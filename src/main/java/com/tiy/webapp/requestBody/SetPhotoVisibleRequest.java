package com.tiy.webapp.requestBody;

/**
 * Created by Paul Dennis on 1/30/2017.
 */
public class SetPhotoVisibleRequest {

    String email;
    Boolean photoVisible;

    public SetPhotoVisibleRequest () {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getPhotoVisible() {
        return photoVisible;
    }

    public void setPhotoVisible(Boolean photoVisible) {
        this.photoVisible = photoVisible;
    }
}
