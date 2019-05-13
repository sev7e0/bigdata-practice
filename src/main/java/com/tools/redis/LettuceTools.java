package com.tools.redis;

public enum LettuceTools {

    URL("redis://localhost:6379/0");

    private String value;

    LettuceTools(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
