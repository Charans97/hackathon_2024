package org.example;

import io.confluent.connect.replicator.offsets.ConsumerTimestampsInterceptor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

public class Consumer {



    public static void main(String[] args) throws InterruptedException {


        Properties p = new Properties();
        try {
            FileInputStream input = new FileInputStream("/home/sumo/hackathon/client.properties");
            p.load(input);
            input.close();
        }catch (IOException e) {
            e.printStackTrace();
            return;
        }

       // p.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9094");
        p.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-4r087.us-west2.gcp.confluent.cloud:9092");
        p.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        p.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        p.setProperty(ConsumerConfig.GROUP_ID_CONFIG , "ConsumerGroup10");
        p.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        System.out.println(p.toString());
        p.setProperty(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, ConsumerTimestampsInterceptor.class.getName());
        final KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(p);

        consumer.subscribe(Arrays.asList("gr"));

        while (true) {
            ConsumerRecords<String, String> records =  consumer.poll(Duration.ofMillis(500));
            for (ConsumerRecord record: records) {
                System.out.println("Received new record: \n" +
                        "Key: " + record.key() + ", " +
                        "Value: " + record.value() + ", " +
                        "Topic: " + record.topic() + ", " +
                        "Offset: " + record.offset() + ", " +
                        "Partition: " + record.partition() + "\n");
                Thread.sleep(1000 * 30);
                consumer.commitSync();
            }

        }

        // interceptor.classes=io.confluent.connect.replicator.offsets.ConsumerTimestampsInterceptor;
    }

}
