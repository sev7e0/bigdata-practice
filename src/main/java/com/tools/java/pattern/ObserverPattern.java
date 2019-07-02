package com.tools.java.pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式(对象行为型模式)
 * <p>
 * 定义：对象间一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于他的对象都能得到通知并被
 * 更行。这种模式有时又称作发布-订阅模式、模型-视图模式，它是对象行为型模式。
 * <p>
 * 优点：降低了目标与观察者的耦合度，两者之间抽象耦合
 * 两者之间建立了触发机制
 * 缺点：目标与观察者之间的依赖关系并没有完全解除，而且有可能出现循环引用。
 * 当观察者对象很多时，通知的发布会花费很多时间，影响程序的效率。
 * <p>
 * 观察者模式的主要角色如下。
 * 抽象主题（Subject)：也叫抽象目标类，它提供了一个用于保存观察者对象的聚集类和增加、删除观察者对象的方法，以及通知所有观察者的抽象方法。
 * 具体主题（Concrete Subject)：也叫具体目标类，它实现抽象目标中的通知方法，当具体主题的内部状态发生改变时，通知所有注册过的观察者对象。
 * 抽象观察者（Observer)：它是一个抽象类或接口，它包含了一个更新自己的抽象方法，当接到具体主题的更改通知时被调用。
 * 具体观察者（Concrete Observer)：实现抽象观察者中定义的抽象方法，以便在得到目标的更改通知时更新自身的状态。
 *
 *
 */
public class ObserverPattern {
    public static void main(String[] args) {
        Subject subject = new ConcreteSubject();
        Observer obs1 = new ConcreteObserver1();
        Observer obs2 = new ConcreteObserver2();
        subject.add(obs1);
        subject.add(obs2);
        subject.notifyObserver();
    }
}

//抽象目标
abstract class Subject {
    protected List<Observer> observers = new ArrayList<Observer>();

    //增加观察者方法
    public void add(Observer observer) {
        System.out.println(observer.getName()+"已经加入队列");
        observers.add(observer);
    }

    //删除观察者方法
    public void remove(Observer observer) {
        observers.remove(observer);
    }

    public abstract void notifyObserver(); //通知观察者方法
}

//具体目标
class ConcreteSubject extends Subject {
    public void notifyObserver() {
        System.out.println("具体目标发生改变...");
        System.out.println("--------------");

        for (Object obs : observers) {
            ((Observer) obs).response();
        }

    }
}

//抽象观察者
interface Observer {
    String getName();
    void response(); //反应
}

//具体观察者1
class ConcreteObserver1 implements Observer {
    @Override
    public String getName() {
        return "ConcreteObserver1";
    }

    public void response() {
        System.out.println("ConcreteObserver1作出反应！");
    }
}

//具体观察者1
class ConcreteObserver2 implements Observer {
    @Override
    public String getName() {
        return "ConcreteObserver2";
    }

    public void response() {
        System.out.println("ConcreteObserver2作出反应！");
    }
}