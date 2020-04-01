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
    public static final String brokerList = "spark01:9092";
    public static final String topic = "randomCount_new";

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

        //acks它代表消息确认机制
        properties.put("acks", "all");
        //重试的次数
        properties.put("retries", 0);
        //批处理数据的大小，每次写入多少数据到topic
        properties.put("batch.size", 2);
        //可以延长多久发送数据
        properties.put("linger.ms", 1);
        properties.put("partitioner.class", "com.tools.kafka.CustomPartitioner");
        //缓冲区的大小
        properties.put("buffer.memory", 33554432);
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        return properties;
    }

    public static void main(String[] args) throws InterruptedException {
        Properties properties = initProperties();

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        Random random = new Random(10);
        for (int i = 0; i < 1000000; i++) {
            String value = "消息内容："+i+"-----"+random.nextInt(10000);
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);
            producer.send(record);
            log.info("已发送：{}条, value为: {}", i, value);
//            Thread.sleep(1000);
        }
        producer.close();
    }

}
