package com.tools.java.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Title:  ReLockTest.java
 * description: ReentrantLock test
 * 				实现三个线程交替打印 线程1 ：1-10  线程2 : 1-10  线程3 : 1-10
 * 				共打印十次。
 *
 * @author: sev7e0
 * @version: 1.0
 * @create: 2020-04-02 09:57
 **/

public class ReentrantLockTest {
	/**
	 * 线程操作资源类
	 * 检查-工作-唤醒其他线程
	 */

	private static ReentrantLock lock = new ReentrantLock();
	private static Condition condition1 = lock.newCondition();
	private static Condition condition2 = lock.newCondition();
	private static Condition condition3 = lock.newCondition();

	private static int number = 1;

	private static void printTask(){
		for (int i = 0; i < 10; i++) {
			System.out.println("当前线程: "+Thread.currentThread().getName()+"--打印: "+i);
		}
	}

	private static void TaskA() {
		try {
			lock.lock();
			while (number != 1){
				condition1.await();
			}
			printTask();
			number = 2;
			System.out.println("----------------------------");
			condition2.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	private static void TaskB() {
		try {
			lock.lock();
			while (number != 2){
				condition2.await();
			}
			printTask();
			number = 3;
			System.out.println("----------------------------");
			condition3.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	private static void TaskC() {
		try {
			//加锁
			lock.lock();
			//检验
			while (number != 3){
				condition3.await();
			}
			//工作
			printTask();
			System.out.println("=============");
			System.out.println("===完成一次===");
			number = 1;
			//唤醒其他线程
			condition1.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			//开锁
			lock.unlock();
		}
	}
	public static void main(String[] args) throws InterruptedException {

		ExecutorService executorService = Executors.newFixedThreadPool(3);
		for (int i = 0; i < 3; i++) {
			executorService.execute(()->{
				Thread.currentThread().setName("A");
				TaskA();
			});
			executorService.execute(()->{
				Thread.currentThread().setName("B");
				TaskB();
			});
			executorService.execute(()->{
				Thread.currentThread().setName("C");
				TaskC();
			});
		}

		/**
		 * awaitTermination内部同样是使用的ReentrantLock + Condition实现的等待
		 *
		 * 使用的是 condition1.awaitNanos()方法
		 */
		executorService.awaitTermination(10, TimeUnit.SECONDS);
		executorService.shutdown();
	}


}
