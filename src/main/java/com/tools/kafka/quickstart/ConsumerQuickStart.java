package com.tools.kafka.quickstart;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class ConsumerQuickStart {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-1";
    public static final String groupId = "group-1";
    public static final String out = "topic={} - partition={} - offset={} - value={}";

    /**
     * 初始化配置
     *
     * @return
     */
    private static Properties initProperties() {
        Properties properties = new Properties();

        //
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return properties;
    }


    public static void main(String[] args) {

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(initProperties());

        consumer.subscribe(Collections.singletonList(topic));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            records.forEach(record ->
                    log.info(out,
                    record.topic(),
                    record.partition(),
                    record.offset(),
                    record.value()));
        }
    }

}

