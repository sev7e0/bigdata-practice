package com.tools.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 客户端消费 多线程方式实现，未完待续
 */
@Slf4j
public class ConsumerThread extends Thread{
    private KafkaConsumer<String, String> kafkaConsumer;

    private ExecutorService executorService;

    private int threadNum;


    public ConsumerThread(Properties properties, String topic, int threadNum) {
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        this.threadNum = threadNum;

        executorService = new ThreadPoolExecutor(
                threadNum,
                threadNum,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(1000),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    public void run() {
        try {
            while (true){
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));

                if (!records.isEmpty()){
                    executorService.submit(new RecordHandler(records));
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            kafkaConsumer.close();
        }

    }
}

class RecordHandler extends Thread{
    private ConsumerRecords<String, String> records;

    private Map<TopicPartition, OffsetAndMetadata> offsets;

    public RecordHandler(ConsumerRecords records){
        this.records = records;
    }

    @Override
    public void run() {
        records.partitions()
                .forEach(partition->{
                    List<ConsumerRecord<String, String>> record = records.records(partition);

                    long lastConsumerOffset = record.get(record.size() - 1).offset();

                    synchronized (offsets){
                        if (!offsets.containsKey(partition)){
                            offsets.put(partition, new OffsetAndMetadata(lastConsumerOffset + 1));
                        }else {
                            long position = offsets.get(partition).offset();
                            if (position <lastConsumerOffset+1){
                                offsets.put(partition, new OffsetAndMetadata(lastConsumerOffset+ 1));
                            }
                        }
                    }
                });
    }
}