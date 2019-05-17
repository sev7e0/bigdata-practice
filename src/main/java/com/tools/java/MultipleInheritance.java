package com.tools.java;

/**
 * 详细介绍java8中多继承、多层继承
 */

public class MultipleInheritance{
    public static void main(String[] args) {

        D2 d2 = new D2();
        //将会调用方法签名参数为String的方法
        d2.hi("String");
        //将会调用方法签名参数为int的方法
        d2.hi(7777);


        D4 d4 = new D4();
        d4.say();

        C5 c5 = new C5();
        c5.hi();
        c5.say();
    }
}


/**
 * 多继承问题
 */
interface A1{
    default void hi(String hi){
        System.out.println(hi);
    }
}
interface B1{
    default void hi(String hi){
        System.out.println(hi);
    }
}
interface C1 extends A1,B1{

    /**
     * 继承的接口有两个默认方法具有相同的方法签名
     * 则C3需要显式重写该方法，否则编译不通过
     *
     * @param hi
     */
    @Override
    default void hi(String hi) {

    }
}


interface A2{
    default void hi(int hi){
        System.out.println(hi);
    }
}
interface B2{
    default void hi(String hi){
        System.out.println(hi);
    }
}
interface C2 extends A2, B2{
    /**
     * 此时这不需要重写，因为方法签名不同，
     * 使用过程中，将会自动选择
     */
}
class D2 implements C2{

}



/**
 * 多层继承的问题
 */

interface A3{
    default void hi(int hi){
        System.out.println(hi);
    }
}
interface B3 extends A3{
    default void hi(String hi){
        System.out.println(hi);
    }
    default void play() {
        System.out.println("B.play");
    }
}
interface C3 extends B3{
    /**
     * C3会继承B3的默认方法，把包含直接定义和覆盖
     * 父类的方法。同时隐式继承A3中的默认方法
     */
}


/**
 * 多层多继承
 */

interface A4 {
    default void say() {
        System.out.println("A4");
    }
}
interface A40 extends A4 {
    default void say() {
        System.out.println("A5");
    }
}
interface B4 extends A4,A40{

    /**
     * 接口B4会隐式继承A40的方法。
     */
}
class D4 implements B4 {

}

/**
 * 类和接口混合继承与实现
 */

class A5{
    public void hi(){
        System.out.println("A5.hi");
    }
}

interface B5{
    default void hi(){
        System.out.println("B5.hi");
    }
    default void say(){
        System.out.println("B5.say");
    }
}

class C5 extends A5 implements B5{
    //c5.hi();
    /**
     * 将会输出A5.hi
     * 子类将会优先继父类的方法，若父类没有相同
     * 签名的方法将会继承接口
     */
}

/**
 * 总结：
 * 1、类在继承时会优先于接口，若父类和接口中同时存在相同的方法实现，那么优先父类方法
 * 2、在多层继承中子类中的方法会优先于父类方法
 * 3、若两个条件，这要求类要实现方法，否则将会编译错误---亦可以将类声明为abstract
 */
