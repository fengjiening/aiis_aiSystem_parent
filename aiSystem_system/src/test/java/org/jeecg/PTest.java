package org.jeecg;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Created by fengjiening on 2020/5/29.
 */
public class PTest {
    public static void main(String []a1 ){
        Integer a =128;
        Integer b =128;

        System.out.println(Integer.max(1,2));

        Algorithm algorithm = Algorithm.HMAC256("1234561");
        JWTVerifier verifier = JWT.require(algorithm).withClaim("username", "admin").build();
        // 效验TOKEN
        DecodedJWT jwt = verifier.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE1OTE2MDU4MjAsInVzZXJuYW1lIjoiYWRtaW4ifQ.XXaU_C0TcXFEKVhTYjvVNzI9EkYsfhNgkm7mNoPFEa4");

    }
}
