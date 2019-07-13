package com.tools.java.pattern;

public class A_3_AbstractFactory {
    public static void main(String[] args) {
        //定义出两个工厂
        AbstractCreator creator1 = new Creator1();
        AbstractCreator creator2 = new Creator2();
        //产生A1对象
        AbstractProductA a1 =  creator1.createProductA();
        //产生A2对象
        AbstractProductA a2 = creator2.createProductA();
        //产生B1对象
        AbstractProductB b1 = creator1.createProductB();
        //产生B2对象
        AbstractProductB b2 = creator2.createProductB();

        a1.doSomething();

        a2.doSomething();

        b1.doSomething();

        b2.shareMethod();
    }
}

abstract class AbstractProductA {
    //每个产品共有的方法
    public void shareMethod(){
        System.out.println(" A类共有方法!");
    }
    //每个产品相同方法，不同实现
    public abstract void doSomething();
}



//产品A1的实现类
class ProductA1 extends AbstractProductA {
    public void doSomething() {
        System.out.println("产品A1的实现方法");
    }
}


//产品A2的实现类
class ProductA2 extends AbstractProductA {
    public void doSomething() {
        System.out.println("产品A2的实现方法");
    }
}


//抽象产品B类
abstract class AbstractProductB {
    //每个产品共有的方法
    public void shareMethod(){
        System.out.println("B类共有方法!");
    }
    //每个产品相同方法，不同实现
    public abstract void doSomething();
}


//产品B1的实现类
class ProductB1 extends AbstractProductB {
    public void doSomething() {
        System.out.println("产品B1的实现方法");
    }
}


//产品B2的实现类
class ProductB2 extends AbstractProductB {
    public void doSomething() {
        System.out.println("产品B2的实现方法");
    }
}


//抽象工厂类
abstract class AbstractCreator {
    //创建A产品家族
    public abstract AbstractProductA createProductA();
    //创建B产品家族
    public abstract AbstractProductB createProductB();
}


//产品等级1的实现类
class Creator1 extends AbstractCreator {
    //只生产产品等级为1的A产品
    public AbstractProductA createProductA() {
        return new ProductA1();
    }
    //只生产产品等级为1的B产品
    public AbstractProductB createProductB() {
        return new ProductB1();
    }
}


//产品等级2的实现类
class Creator2 extends AbstractCreator {
    //只生产产品等级为2的A产品
    public AbstractProductA createProductA() {
        return new ProductA2();
    }
    //只生产产品等级为2的B产品
    public AbstractProductB createProductB() {
        return new ProductB2();
    }
}