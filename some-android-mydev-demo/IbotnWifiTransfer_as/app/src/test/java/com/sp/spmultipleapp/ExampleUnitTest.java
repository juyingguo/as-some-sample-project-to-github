package com.sp.spmultipleapp;

import android.net.Uri;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void testCalendarTime() throws Exception {
        Calendar c1 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY,17);
        c1.set(Calendar.MINUTE,0);
        c1.set(Calendar.SECOND,0);
        c1.set(Calendar.MILLISECOND,0);

        Calendar c2 = Calendar.getInstance();
        c2.set(Calendar.HOUR_OF_DAY,12);
        c2.set(Calendar.MINUTE,30);
        c2.set(Calendar.SECOND,0);
        c2.set(Calendar.MILLISECOND,0);

        Calendar c3 = Calendar.getInstance();
        c3.set(Calendar.HOUR_OF_DAY,12);
        c3.set(Calendar.MINUTE,30);
        c3.set(Calendar.DAY_OF_YEAR,c3.get(Calendar.DAY_OF_YEAR) + 1);
//        c3.set(Calendar.DAY_OF_WEEK,c3.get(Calendar.DAY_OF_WEEK) + 1);
        c3.set(Calendar.SECOND,0);
        c3.set(Calendar.MILLISECOND,0);

        System.out.println("c1:" + c1 );
        System.out.println("c1>>getTimeInMillis:" + c1.getTimeInMillis() );
        System.out.println("c2>>getTimeInMillis:" + c2.getTimeInMillis() );
        System.out.println("c3>>getTimeInMillis:" + c3.getTimeInMillis() );

    }
    @Test
    public void testUri(){
        System.out.println("Uri.EMPTY:" + Uri.EMPTY);//测试情况：为null。
    }
}