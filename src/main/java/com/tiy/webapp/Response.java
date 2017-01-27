package com.tiy.webapp;

/**
 * Created by Paul Dennis on 1/27/2017.
 */
public class Response {

    boolean success;

    public Response() {

    }

    public Response (boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
