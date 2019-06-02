package com.tools.java;

public class Complex {

    private final double re;

    private final double im;

    private Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * 在保证不可变性，则类绝对不允许自身被之类化，除了使用final
     * 修饰类以外，还有一种方式可以使用。那就是将类所有的构造器都
     * 变成私有的，如上，同时提供一个公有的静态工厂来代替，这将是
     * 最好的代替方法。
     */

    public static Complex valueOf(double re, double im){
        return new Complex(re, im);
    }
}
