package com.tiy.webapp;

import javax.persistence.Column;
import javax.persistence.Lob;

/**
 * Created by Paul Dennis on 1/27/2017.
 */
public class ImageString {

    @Column(nullable = true)
    @Lob
    String imageString;

}
