package com.tools.java;

import java.util.Arrays;
import java.util.EmptyStackException;

public class Stack {

    private Object[] elem;

    //要优先使用基本类型，防止不必要的自动装箱，会创建多余对象
    private int size = 0;

    private static final int DEFULT = 16;


    public void push(Object object){
        ensure();
        elem[size++] = object;
    }

    /**
     * 在对象出栈后，要及时清除过期引用，防止内存泄漏。
     *
     * 对于垃圾回收来说，数组中所有对象的引用都是同等
     * 有效的，只有开发人员自己清楚，对象的重要部分，
     * 那么就需要开发人员告诉虚拟机，这部分可以被清理
     * 掉，方式就是自己手动清理。
     *
     * @return
     */
    public  Object pop(){
        if (size == 0)
            throw new EmptyStackException();
        Object o = elem[size];
        elem[size--] = null;
        return o;

    }
    //重点：只要内存是自己管理的，那么就需要注意内存泄漏的问题。

    private void ensure(){
        if (elem.length == size){
            elem = Arrays.copyOf(elem, size*2);
        }
    }
}
