package com.tools.kafka.quickstart;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * kafka生产者。
 *
 * 主要三大组件：
 * - 序列化器 -> 必须配置
 * - 分区器 -> 选配
 * - 生产者拦截器 -> 选配
 *
 * 全部配置的情况下执行顺序为 生产者拦截器 -> 序列化器 -> 分区器
 */
public class ProducerQuickStart {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-1";

    /**
     * 初始化参数
     *
     * @return
     */
    private static Properties initProperties() {
        Properties properties = new Properties();

        //生产者需要序列化器将对象转换成字节数组才能通过网络发送给kafka服务端
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
//        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);

        return properties;
    }

    public static void main(String[] args) throws InterruptedException {

        //构建producer实例。
        KafkaProducer<String, String> producer = new KafkaProducer<>(initProperties());

        for (int i = 100; i < 1000; i++) {

            //构建消息实例ProducerRecord
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "hello kafka-" + i);

            //消息发送
            producer.send(record);

            Thread.sleep(5000);
        }


        producer.close();
    }

}

