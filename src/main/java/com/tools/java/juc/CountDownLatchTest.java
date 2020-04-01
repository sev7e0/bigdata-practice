package com.tools.java.juc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Title:  CountdownLatchTest.java
 * description: CountDownLatch test
 *
 * @author: sev7e0
 * @version: 1.0
 * @create: 2020-04-01 16:00
 **/

public class CountDownLatchTest {


	/**
	 * 当前线程：pool-1-thread-1--倒数：8
	 * 当前线程：pool-1-thread-2--倒数：8
	 * 当前线程：pool-1-thread-2--倒数：7
	 * 当前线程：pool-1-thread-1--倒数：6
	 * 当前线程：pool-1-thread-2--倒数：5
	 * 当前线程：pool-1-thread-1--倒数：4
	 * 当前线程：pool-1-thread-2--倒数：3
	 * 当前线程：pool-1-thread-1--倒数：2
	 * 当前线程：pool-1-thread-2--倒数：1
	 * 当前线程：pool-1-thread-1--倒数：0
	 * end!
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(10);

		ExecutorService executorService = Executors.newFixedThreadPool(2);

		for (int i = 0; i < 10; i++) {
			executorService.execute(() -> {
				countDownLatch.countDown();
				System.out.println("当前线程：" + Thread.currentThread().getName() + "--倒数：" + countDownLatch.getCount());
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		//
		countDownLatch.await();

		System.out.println("end!");

		executorService.shutdown();


	}
}
