package com.tools.java;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Title:sparklearn
 * description:
 *
 * @author: Lijiaqi
 * @version: 1.0
 * @create: 2018-12-05 11:13
 **/

public class FutureTaskTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> integerFutureTask = new FutureTask<>(() -> {
            int result = 0;
            for (int i = 0; i < 100; i++) {
                System.out.println("FutureTask is running");
                Thread.sleep(100);
                result += i;
            }
            return result;
        });
        Thread thread = new Thread(integerFutureTask);
        thread.start();
        //
        Thread thread1 = new Thread(() -> {
            System.out.println("other task is running...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        //会将当前线程阻塞,直到get()方法有返回
        System.out.println(integerFutureTask.get());
        System.out.println("i will be block");

        unSafe();
    }

    public static class ThreadUnsafeExample {

        private int cnt = 0;

        public void add() {
            cnt++;
        }

        public int get() {
            return cnt;
        }
    }

    public static void unSafe() throws InterruptedException {
        final int threadSize = 1000;
        ThreadUnsafeExample example = new ThreadUnsafeExample();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < threadSize; i++) {
            executorService.execute(example::add);
        }
        executorService.shutdown();
        System.out.println(example.get());
    }
}
