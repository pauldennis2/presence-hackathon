package com.tiy.webapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by Paul Dennis on 1/27/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ComplexRepoTests {

    @Autowired
    EventRepo events;

    @Autowired
    ContactRepo contacts;

    @Autowired
    UserRepo users;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    public void complexTests () {
        System.out.println("Add some more complex tests, fool");
        assertTrue(false);
    }

    @Test
    public void testPictureLob () {
        ImageString imageString = new ImageString();
        try {
            String content = new Scanner(new File("base64test.txt")).useDelimiter("\\Z").next();
            System.out.println("Content length: " + content.length());
            imageString.setImageString(content);
        } catch (Exception exception) {
            System.out.println("Unable to read file with exception: " + exception.getMessage());
        }
    }

}