# 关于synchronized和ReentrantLock的记录

## 系统层面

synchronized是jvm关键字,底层使用的是monitorenter和monitorexit

ReentrantLock是提供的类对象

## 中断

当死锁发生时ReentrantLock可以使用中断，而synchronized不可中断。

## 公平锁

synchronized只能创建非公平锁，而reentrantLock支持添加参数创建公平锁。

## 支持condition

ReenTrantLock提供了一个Condition（条件）类，用来实现分组唤醒需要唤醒的线程们，而不是像synchronized要么随机唤醒一个线程要么唤醒全部线程。

## 锁绑定多个条件

一个ReentrantLock对象可以同时绑定对个对象