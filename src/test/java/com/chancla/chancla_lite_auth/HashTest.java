package com.chancla.chancla_lite_auth;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashTest {
    @Test
    public void generateHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("HASH_FOR_TIENDA:" + encoder.encode("tienda"));
        System.out.println("HASH_FOR_ADMIN:" + encoder.encode("admin"));
        System.out.println("HASH_FOR_ADMIN123:" + encoder.encode("admin123"));
    }
}
