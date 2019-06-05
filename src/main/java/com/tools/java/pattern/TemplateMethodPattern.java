package com.tools.java.pattern;

/**
 * 模版方法模式
 * 什么是模版方法模式：
 *      是类的行为模式，
 */
public class TemplateMethodPattern {
    public static void main(String[] args) {
        MacBook macBook = new MacBook();
        IPad iPad = new IPad();
        macBook.startComputer();
        iPad.startComputer();
    }
}

interface Computer{

    void power();

    void mainBoard();

    void cpu();

    void hardDisk();

    void display();
}

abstract class AbstractComputer implements Computer{

    public void startComputer(){
        power();
        mainBoard();
        cpu();
        hardDisk();
        display();

    }

    public final void power(){
        System.out.println("开始供电");
    }

    public abstract void mainBoard();

    public abstract void cpu();

    public abstract void hardDisk();

    public final void display(){
        System.out.println("已启动");
    }
}

class MacBook extends AbstractComputer implements Computer{


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

}

class IPad extends AbstractComputer implements Computer{


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

}