package com.kaituo.comparison.back;

import java.util.UUID;

public class Demo {
    public static void main(String[] args) {
        String replace = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        System.out.println(replace);
    }
}
