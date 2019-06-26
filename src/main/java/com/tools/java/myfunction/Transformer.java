package com.tools.java.myfunction;


/**
 * Custom Function Interface
 *
 * @param <T>
 */

@FunctionalInterface
public interface Transformer<T> {

    T transformer(T input);

    //you can use default method
    default void sayHi() {
        System.out.println("hi this is my functional interface!");
    }

    //use static method
    static void sayHello() {
        System.out.println("Hello this is static method of my functional interface !");
    }
}
