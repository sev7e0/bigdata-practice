package com.tools.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

/**
 * ReBalance监听器的用法，如何做到减少重复消费。
 */
@Slf4j
public class ConsumerReBalance {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-1";
    //新的group，相较于ConsumerQuickStart group-1分组，现在kafka是发布订阅模型
    public static final String groupId = "group-3";
    public static final String out = "topic={} - partition={} - offset={} - value={}";

    /**
     * 初始化配置
     *
     * @return
     */
    private static Properties initProperties() {
        Properties properties = new Properties();

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        //关闭kafka默认的自动提交offset，容易导致重复处理的问题
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return properties;
    }


    public static void main(String[] args) {

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(initProperties());

        Map<TopicPartition, OffsetAndMetadata> map = new HashMap<>();
        consumer.subscribe(Collections.singletonList(topic), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                //同步提交
                consumer.commitSync(map);
                //亦可以选择存储到DB中。
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {

            }
        });

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                records.forEach(record -> {
                            log.info(out,
                                    record.topic(),
                                    record.partition(),
                                    record.offset(),
                                    record.value());
                            ///将offset存储到局部变量中，在ReBalance发生前，能够同步的提交offset避免重复消费
                            map.put(new TopicPartition(record.topic(), record.partition()),
                                    new OffsetAndMetadata(record.offset() + 1));
                        }
                );
                //异步提交offset
                consumer.commitAsync(map, null);
            }
        } finally {
            //使用同步提交，做最后的把关
            consumer.commitSync();
            consumer.close();
        }


    }

}

