package com.tools.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * 手动控制提交offset
 */
@Slf4j
public class ConsumerCommitOffset {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-1";
    //新的group，相较于ConsumerQuickStart group-1分组，现在kafka是发布订阅模型
    public static final String groupId = "group-2";
    public static final String out = "topic={} - partition={} - offset={} - value={}";

    /**
     * 初始化配置
     *
     * @return
     */
    private static Properties initProperties() {
        Properties properties = new Properties();

        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        //关闭kafka默认的自动提交offset，容易导致重复处理的问题
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return properties;
    }


    public static void main(String[] args) {

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(initProperties());

        consumer.subscribe(Collections.singletonList(topic));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                records.forEach(record ->
                        log.info(out,
                                record.topic(),
                                record.partition(),
                                record.offset(),
                                record.value()));
                //异步提交offset
                consumer.commitAsync();
            }
        }finally {
            //使用同步提交，做最后的把关
            consumer.commitSync();
            consumer.close();
        }


    }

}

