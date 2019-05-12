package com.tools.java;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Title:sparklearn
 * description:
 *
 * @author: Lijiaqi
 * @version: 1.0
 * @create: 2018-12-04 18:29
 **/

public class SemaphoreTest {

    public static void main(String[] args) {
        int clientNum = 10;
        int semaphoreNum = 5;
        Semaphore semaphore = new Semaphore(semaphoreNum);
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < clientNum; i++) {
            threadPool.execute(() -> {
                try {
                    semaphore.acquire();
                    System.out.println(semaphore.availablePermits());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                semaphore.release();
            });
        }

    }
}
