package com.tools.java.pattern;

/**
 * 模版方法模式
 *
 * 什么是模版方法模式：
 *      是类的行为模式，定义一个算法骨架，而将一些步骤延迟到子类中实现，
 *      模版方法使得子类可以不改变算法的结构，而重新定义该算法的具体实现。
 *
 * 适用性：
 *      - 一次性实现一个算法不变的部分，改变的部分交给子类实现
 *      - 控制子类拓展，只有允许子类实现的方法，子类才可以实现。
 *      - 从所有子类中抽取公共方法，交给父类实现，减少代码重复。
 *
 * 所需角色：
 *      AbstractClass 抽象类
 *      ConcreteClass 具体类
 *
 * 效果：
 *      模版方法是代码复用的基本技术，可以提取出类中的公有行为。
 *      模版方法导致一种方向控制结构，这种结构有时被称为"好莱坞法则"，这里
 *      指的是父类对子类操作的调用。
 *
 * 实现：
 *      参考下方实现。
 *
 * 已知应用：
 *      Set，AbstractSet，HashSet
 *
 * 相关模式：
 *      Factory Method、Strategy等模式。
 *
 * http://blog.sev7e0.site/article/59#comment-box
 */
public class TemplateMethodPattern {
    public static void main(String[] args) {
        MacBook macBook = new MacBook();
        IPad iPad = new IPad();
        macBook.startComputer();
        System.out.println("-----------");
        iPad.setDisplay(false);
        iPad.startComputer();
        /**
         * 开始供电
         * MacBook启动主板
         * MacBook开始为CPU供电
         * MacBook开始为硬盘供电
         * MacBook屏幕点亮
         * -----------
         * 用户要求 iPad 屏幕不点亮
         * 开始供电
         * IPad启动主板
         * IPad开始为CPU供电
         * IPad开始为硬盘供电
         */
    }
}

abstract class AbstractComputer {
    /**
     * final禁止被重新定义
     */
    final protected void startComputer(){
        power();
        mainBoard();
        cpu();
        hardDisk();
        if(isDisplay()){
            display();
        }
    }

    final public void power(){
        System.out.println("开始供电");
    }

    /**
     * 抽象操作必须被重新定义
     */
    public abstract void mainBoard();

    public abstract void cpu();

    public abstract void hardDisk();

    public abstract void display();
    /**
     * hook函数可以被重新定义,默认返回 true，所有设备都开启显示
     */
    public boolean isDisplay(){
        return true;
    }
}

class MacBook extends AbstractComputer{


    @Override
    public void mainBoard() {
        System.out.println("MacBook启动主板");
    }

    @Override
    public void cpu() {
        System.out.println("MacBook开始为CPU供电");
    }

    @Override
    public void hardDisk() {
        System.out.println("MacBook开始为硬盘供电");
    }

    @Override
    public void display() {
        System.out.println("MacBook屏幕点亮");
    }
}

class IPad extends AbstractComputer{

    private boolean flag = true;
    @Override
    public void mainBoard() {
        System.out.println("IPad启动主板");
    }

    @Override
    public void cpu() {
        System.out.println("IPad开始为CPU供电");
    }

    @Override
    public void hardDisk() {
        System.out.println("IPad开始为硬盘供电");
    }

    @Override
    public void display() {
        System.out.println("IPad屏幕点亮！");
    }

    @Override
    public boolean isDisplay(){
        return this.flag;
    }
    /**
     * 用户自定义是否点亮屏幕
     */
    public void setDisplay(boolean userFlag){
        if (!userFlag){
            System.out.println("用户要求 iPad 屏幕不点亮");
        }
        this.flag = userFlag;
    }
}
