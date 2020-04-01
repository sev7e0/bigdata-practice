package com.tools.java;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class demo {

    public static void main(String[] args) {
//        log.warn("ss",new Throwable("13213213"));
        log.warn("ss{}{}","ddddd","aaaa",new Throwable("13213213"));
    }
}