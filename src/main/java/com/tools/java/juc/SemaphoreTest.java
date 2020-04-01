package com.tools.java.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Title:sparklearn
 * description: 信号量test
 *
 * @author: Lijiaqi
 * @version: 1.0
 * @create: 2018-12-04 18:29
 **/

public class SemaphoreTest {

	/**
	 * pool-1-thread-1 获取到资源：3
	 * pool-1-thread-5 获取到资源：0
	 * pool-1-thread-4 获取到资源：1
	 * pool-1-thread-3 获取到资源：2
	 * pool-1-thread-2 获取到资源：3
	 * pool-1-thread-5--3妙后离开
	 * pool-1-thread-2--3妙后离开
	 * pool-1-thread-3--3妙后离开
	 * pool-1-thread-4--3妙后离开
	 * pool-1-thread-1--3妙后离开
	 * pool-1-thread-9 获取到资源：0
	 * pool-1-thread-10 获取到资源：0
	 * pool-1-thread-8 获取到资源：0
	 * pool-1-thread-7 获取到资源：0
	 * pool-1-thread-6 获取到资源：1
	 * pool-1-thread-9--3妙后离开
	 * pool-1-thread-10--3妙后离开
	 * pool-1-thread-8--3妙后离开
	 * pool-1-thread-7--3妙后离开
	 * pool-1-thread-6--3妙后离开
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		int clientNum = 10;
		int semaphoreNum = 5;
		Semaphore semaphore = new Semaphore(semaphoreNum);
		ExecutorService threadPool = Executors.newFixedThreadPool(10);
		for (int i = 0; i < clientNum; i++) {
			threadPool.execute(() -> {
				try {
					semaphore.acquire();
					System.out.println(Thread.currentThread().getName() + "----获取到资源, 当前剩余" + semaphore.availablePermits());
					Thread.sleep(3000);
					System.out.println(Thread.currentThread().getName() + "----3妙后离开");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					semaphore.release();
				}
			});
		}
		threadPool.shutdown();
	}
}
