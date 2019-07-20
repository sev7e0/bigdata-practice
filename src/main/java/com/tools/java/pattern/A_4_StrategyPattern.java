package com.tools.java.pattern;

/**
 * 策略模式
 */
public class A_4_StrategyPattern {
    public static void main(String[] args) {

        Strategy strategy1 = new ConcreteStrategy1();
        Strategy strategy2 = new ConcreteStrategy2();

        new StrategyContext(strategy1).executeStrategy();

        new StrategyContext(strategy2).executeStrategy();

    }
}

interface Strategy{

    void doSomething();
}

class ConcreteStrategy1 implements Strategy{

    @Override
    public void doSomething() {
        System.out.println("this is strategy1");
    }
}

class ConcreteStrategy2 implements Strategy{

    @Override
    public void doSomething() {
        System.out.println("this is strategy2");
    }
}

class StrategyContext {

    private Strategy strategy;

    public StrategyContext(Strategy strategy){
        this.strategy = strategy;
    }

    public void executeStrategy(){
        this.strategy.doSomething();
    }

}