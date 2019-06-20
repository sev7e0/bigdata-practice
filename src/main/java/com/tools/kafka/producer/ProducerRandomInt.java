package com.tools.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;

/**
 * 向kafka中发送随机数
 */
@Slf4j
public class ProducerRandomInt {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "randomCount";

    /**
     * 初始化参数
     *
     * @return
     */
    private static Properties initProperties() {
        Properties properties = new Properties();

        //生产者需要序列化器将对象转换成字节数组才能通过网络发送给kafka服务端
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        return properties;
    }

    public static void main(String[] args) throws InterruptedException {
        Properties properties = initProperties();

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        Random random = new Random(10);
        for (int i = 0; i < 1000; i++) {
            String value = String.valueOf(random.nextInt(10));
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
            producer.send(record);
            log.info("已发送：{}条, value为: {}", i, value);
            Thread.sleep(1000);
        }
        producer.close();
    }

}
