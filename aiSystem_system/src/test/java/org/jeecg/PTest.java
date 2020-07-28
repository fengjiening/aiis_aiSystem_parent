package org.jeecg;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.MD5Util;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.modules.demo.test.entity.JeecgDemo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by fengjiening on 2020/5/29.
 */

public class PTest {
    public static void main(String[] a1) {
//        Integer a =128;
//        Integer b =128;

//        System.out.println(Integer.max(1,2));
//
//        Algorithm algorithm = Algorithm.HMAC256("1234561aa");
//        JWTVerifier verifier = JWT.require(algorithm).withClaim("username", "admin").build();
//        // 效验TOKEN
//        DecodedJWT jwt = verifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTE2MDU4MjAsInVzZXJuYW1lIjoiYWRtaW4ifQ.XXaU_C0TcXFEKVhTYjvVNzI9EkYsfhNgkm7mNoPFEa4");
//        List<String> a  = new ArrayList<>();
//        Map<String,String> b =new HashMap<>();
//        //b.put();
//        a.add("3");
//        System.err.println(a);
//        a.set(0,"1");
//


        System.err.println(MD5Util.MD5Encode("880929149083972237914776863702807336125956730618811702346624864661"+MD5Util.SECRET,null));
        System.err.println("b47b01b6eb96b2b90d1213dde78854f3");

        Stream<String> stream = Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> s.startsWith("a"));

        stream.anyMatch(s -> true); // ok stream.noneMatch(s -> true); // exception

        Stream.of("d2", "a2", "b1", "b3", "c").filter(s -> s.startsWith("a")).forEach(System.err::println);

        String a = "abc";
        String b = "abc";
        String c = new String("abc");
        String d = new String("abc");
        System.err.println(a == b);
        System.err.println(a == c);
        System.err.println(c == d);
        System.err.println(DateUtils.getMillis());


        Integer e =123;
        Integer f =123;
        Integer g =new Integer(123);
        System.err.println("============================");
        System.err.println(e == f);
        System.err.println(e == g);



    }
//    public static void main(String[] args){
//        try{
//            long i=getTime("2019-05-29 10:29:00","2019-05-29 11:29:33");
//            System.out.println("您传入的两个时间相差"+i+"分钟！");
//        }catch(Exception e){e.printStackTrace();}
//    }


}


