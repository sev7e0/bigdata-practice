package com.tools.java.pattern;

import lombok.extern.slf4j.Slf4j;


/**
 * 静态工厂方法几大优点：
 *
 * 1.有单独的名称，代码可读性高。
 * 2.不比在每次调用时创建新的对象，参考上方 Boolean.valueOf(false)
 * 3.他们可以返回原返回类型的任意子对象,详见下方代码 31行
 * 4.返回对象的类，可以随着每次调用而发生变化，这取决于静态工厂方法的参数
 * 5.方法返回的对象所属的类，在编写包含该静态工厂方法的类时可以不存在。
 *
 *
 * 缺点：1.类如果不含邮共有的或者受保护的构造器，那么不能够被子类化
 *      2.难被发现。
 *
 * 总结：总结静态工厂方法与构造器都有各自的好处，不过在开发过程中首先
 *      要想到的是静态工厂方法
 * @return
 */

@Slf4j
public class StaticFactoryMethod {
    public static void main(String[] args) {
        StaticFactoryMethod.of(true).print();
        StaticFactoryMethod.of(false).print();

        log.info(Boolean.valueOf(false).toString());
    }

    public static StaticFactoryMethod of(Boolean option){
        if (option){
            return new child();
        }
        return new StaticFactoryMethod();
    }

    public void print(){
        log.info("hello word,hello static factory method.");
    }
}
@Slf4j
class child extends StaticFactoryMethod{

    @Override
    public void print(){
        log.info("hello word,hello static factory method. i m child");
    }
}
