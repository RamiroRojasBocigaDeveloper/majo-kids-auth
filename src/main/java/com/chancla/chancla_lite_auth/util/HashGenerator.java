package com.chancla.chancla_lite_auth.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "tienda";
        String hash = encoder.encode(password);
        System.out.println("HASH_START:" + hash + ":HASH_END");
    }
}
