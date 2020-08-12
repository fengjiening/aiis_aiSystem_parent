package org.jeecg;


import org.jeecg.common.util.MD5Util;

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


        String s = MD5Util.MD5Encode("ddd" + MD5Util.SECRET, null);
        System.err.println(s);
    }
}
