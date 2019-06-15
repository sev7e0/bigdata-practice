package com.tools.kafka.quickstart;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerQuickStart {
    public static final String brokerList = "spark01:9092,spark02:9092,spark02:9092";
    public static final String topic = "topic-1";

    public static void main(String[] args) throws InterruptedException {
        Properties properties = new Properties();

        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("bootstrap.servers", brokerList);

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);



        for (int i = 100; i < 1000; i++) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "hello kafka-"+i);
            producer.send(record);

            Thread.sleep(5000);
        }



        producer.close();
    }

}

