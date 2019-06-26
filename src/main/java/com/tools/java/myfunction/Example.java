package com.tools.java.myfunction;

import java.util.Arrays;
import java.util.Comparator;

/**
 * test
 */
public class Example {

    public static void main(String[] args) {
        Order order = new Order(Arrays.asList(new OrderItem(1, 1225),
                new OrderItem(2, 983),
                new OrderItem(3, 1554)));


        order.transformAndPrint(o -> o.sorted(Comparator.comparing(OrderItem::getPrice)));


        order.transformAndPrintByFunction(
                orderItemStream -> orderItemStream.sorted(Comparator.comparing(OrderItem::getPrice)));
    }
}
