package com.tools.java.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 主要用来测试随机写和顺序写的性能。
 */
public class FileChannelDemo {
    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
//        unSynchronized();
        useSynchronized();
    }


    /**
     * unSynchronized during time：156
     *
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    public static void unSynchronized() throws FileNotFoundException, InterruptedException {
        long start = System.currentTimeMillis();
        FileChannel fileChannel = new RandomAccessFile("db.data", "rw").getChannel();
        ExecutorService executorService = Executors.newFixedThreadPool(64);

        AtomicLong atomicLong = new AtomicLong(0);

        for (int i = 0; i < 1024; i++) {
            executorService.execute(() -> {
                try {
                    fileChannel.write(ByteBuffer.allocate(4 * 1024), atomicLong.getAndAdd(4 * 1024));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        System.out.println("unSynchronized during time：" + (System.currentTimeMillis() - start));
    }


    /**
     * useSynchronized during time：96
     *
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    public static void useSynchronized() throws FileNotFoundException, InterruptedException {
        long start = System.currentTimeMillis();
//        ReentrantLock reentrantLock = new ReentrantLock();
        FileChannel fileChannel = new RandomAccessFile("db1.data", "rw").getChannel();

        ExecutorService executorService = Executors.newFixedThreadPool(64);

        AtomicLong atomicLong = new AtomicLong(0);

        for (int i = 0; i < 1024; i++) {
            final int index = i;
            executorService.execute(() -> {
                try {
                    write(atomicLong, fileChannel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        System.out.println("useSynchronized during time：" + (System.currentTimeMillis() - start));
    }

    private static synchronized void write(AtomicLong atomicLong, FileChannel fileChannel) throws IOException {
        fileChannel.write(ByteBuffer.allocate(4 * 1024), atomicLong.getAndAdd(4 * 1024));
    }
}
