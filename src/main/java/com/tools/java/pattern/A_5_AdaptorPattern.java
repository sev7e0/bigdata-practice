package com.tools.java.pattern;

/**
 * 适配器模式
 */
public class A_5_AdaptorPattern {

    public static void main(String[] args) {
        Target target = new ConcreteTarget();
        target.getPort();
        target.getHost();

        /**
         * 添加了适配器后的改变
         */
        Target adaptor = new Adaptor();
        adaptor.getHost();
        adaptor.getPort();
    }
}

interface Target{

    void getPort();

    void getHost();
}

/**
 * 具体 Target
 */
class ConcreteTarget implements Target{

    @Override
    public void getPort() {

    }

    @Override
    public void getHost() {

    }
}


/**
 * 需要被适配的类
 */
class Adaptee{

    // 可以看到方法名与 Target 不一致
    void port(){
        System.out.println("port is : 65535");
    }

    void host(){
        System.out.println("host is : localhost");
    }
}

/**
 * 适配器 通过实现和继承来达到适配的目标
 */
class Adaptor extends Adaptee implements Target{

    @Override
    public void getPort() {
        //调用父类中的方法
        super.port();
    }

    @Override
    public void getHost() {
        super.host();
    }
}
