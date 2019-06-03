package com.tools.java;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 《Effective Java》第十八条：复合优先于继承（修饰器模式）
 *
 *  为什么不推荐使用继承？
 *  继承破坏了封装性，子类继承了超类依赖于她的一些细节，那么在超类的版本变动
 *  中，她的改动将会影响到子类，就算没有什么代码逻辑变化，那么子类也需要进行
 *  同步的重构，这在同包级的条件下还好，但如果在跨包调用时，将会产生巨大的影响。
 *
 *  什么是修饰器模式？
 *  能够动态的给一个对象添加额外的新工呢个，同比与继承更加灵活。他允许现有的对象
 *  添加新的功能而又不改变其结构。
 *
 *
 *
 * 装饰器模式中的角色：
 * 抽象组件：定义一个接口，在下边的例子中就是Set
 * 具体组件：接口的实现，可以有多个比如HashSet、TreeSet等等
 * 抽象装饰器：接口的实现，并且持有一个私有域，引用了类（Set）的一个实例，private final Set<E> s;所有的方法都是使用的私有域的调用
 * 装饰器：给具体组件添加附加功能的工具，例子中就是InstrumentSet类他为每个set实现添加了以操作数功能。
 *
 *
 * 优点：装饰类和被装饰类都只关心自身的核心业务，实现解藕
 *      方便动态的拓展功能，并且提供了比继承更多的灵活性。
 * 缺点：会产生类膨胀
 *      多层装饰较为复杂
 */
@Slf4j
public class CombinPriority{
    public static void main(String[] args) {
        InstrumentSet<Integer> integers = new InstrumentSet<>(new HashSet<>(5));
        //可以进行多次装饰
        InstrumentSet<Integer> integers1 = new InstrumentSet<>(integers);

        integers.add(1);
        integers.add(1);
        integers.add(1);
        integers.add(1);
        integers.add(1);
        integers.addAll(Arrays.asList(1,1,1,1,1,1,1));
        log.info(String.valueOf(integers.getCount()));


    }

}

/**
 * 装饰器类，覆盖两种添加操作，加入自己的实现，并且调用父类方法。
 *
 * 装饰器类可以由多个，进行不同工能的装饰
 * @param <E>
 */
class InstrumentSet<E> extends Forwarding<E>{

    private int count;

    public InstrumentSet(Set<E> s) {
        super(s);
    }

    @Override
    public boolean add(E o) {
        count++;
        return super.add(o);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        count += c.size();
        return super.addAll(c);
    }

    public int getCount(){
        return count;
    }
}

/**
 * 实现Set接口，也就是上文提到的'抽象装饰器'
 * @param <E>
 */
class Forwarding<E> implements Set<E> {

    //定义一个set的私有域，所有的操作都基于这个set实例
    private final Set<E> s;

    public Forwarding(Set<E> s) {
        this.s = s;
    }

    @Override
    public int size() {
        return s.size();
    }

    @Override
    public boolean isEmpty() {
        return s.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return s.contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return s.iterator();
    }

    @Override
    public Object[] toArray() {
        return s.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return s.toArray(a);
    }

    @Override
    public boolean add(E e) {
        return s.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return s.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return s.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return s.addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return s.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return s.removeAll(c);
    }

    @Override
    public void clear() {
        s.clear();
    }

    @Override
    public int hashCode() {
        return s.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return s.equals(obj);
    }

    @Override
    public String toString() {
        return s.toString();
    }
}

