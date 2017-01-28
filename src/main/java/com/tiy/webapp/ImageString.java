package com.tiy.webapp;

import javax.persistence.Column;
import javax.persistence.Lob;

/**
 * Created by Paul Dennis on 1/27/2017.
 */
public class ImageString {


    String imageString;
    String fileName;

    public ImageString() {
    }

    public ImageString(String imageString, String fileName) {
        this.imageString = imageString;
        this.fileName = fileName;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
