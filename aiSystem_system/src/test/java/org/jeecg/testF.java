package org.jeecg;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;

/**
 * Created by fengjiening on 2020/7/16.
 */
public class testF {

    public static void main (String [] a ) throws Exception{


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String format = df.format("2020-07-29" + " 21:30:00");
        long normalTime = df.parse("2020-07-30" + " 10:35:00").getTime();
        System.err.println(normalTime);
    }
}
