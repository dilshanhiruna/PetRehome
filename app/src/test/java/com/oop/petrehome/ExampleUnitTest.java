package com.oop.petrehome;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import PostAd.DisplayDogAd;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    //IT19963884
    private DisplayDogAd displayDogAd;

    @Before
    public void setUp(){
        //IT19963884
        displayDogAd = new DisplayDogAd();

    }

    //IT19963884
    @Test
    public void correct_date_diff() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date1 =sdf.parse("01-05-2021");
        Date date2 =sdf.parse("07-05-2021");
        assert date1 != null;
        assert date2 != null;
        Long diff =displayDogAd.dateDifferent(date1,date2);
        assertEquals(6,diff.longValue());
    }





}