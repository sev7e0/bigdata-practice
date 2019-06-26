package com.tools.java.myfunction;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class Order {
    List<OrderItem> items;

    public Order(List<OrderItem> orderItems) {
        items = orderItems;
    }

    /**
     * param use custom function interface
     *
     * @param transformOrderItems
     */
    public void transformAndPrint(Transformer<Stream<OrderItem>> transformOrderItems) {
        transformOrderItems.transformer(items.stream()).forEach(System.out::println);
        //call default method
        transformOrderItems.sayHi();
        //call static method
        Transformer.sayHello();
    }

    /**
     * use {@link Function} replace custom function interface
     *
     * @param function
     */
    public void transformAndPrintByFunction(Function<Stream<OrderItem>, Stream<OrderItem>> function) {
        function.apply(items.stream()).forEach(System.out::println);
    }
}