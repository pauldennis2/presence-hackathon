package com.tiy.webapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by Paul Dennis on 2/1/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPersonTest {

    /*@Autowired
    TestPersonRepo testPersons;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void addValues() {
        if (testPersons.count() == 0) {
            TestPerson tos = new TestPerson("James", "Kirk", "Starship Captain");
            TestPerson tng = new TestPerson("Jean-Luc", "Picard", "Starship Captain");
            TestPerson voy = new TestPerson("Catherine", "Janeway", "Starship Captain");
            TestPerson ds9 = new TestPerson("Benjamin", "Sisko", "Space Station Commander");
            TestPerson ent = new TestPerson("Jonathan", "Archer", "Early Starship Captain");
            testPersons.save(tos);
            testPersons.save(tng);
            testPersons.save(voy);
            testPersons.save(ds9);
            testPersons.save(ent);
        }
        assertTrue(testPersons.count() > 0);
    }*/
}