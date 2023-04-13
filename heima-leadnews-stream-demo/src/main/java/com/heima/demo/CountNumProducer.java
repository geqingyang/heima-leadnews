package com.heima.demo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class CountNumProducer {

    public static final String TOPIC = "count-num-stream-topic";
    private static final String KAFKA_IP = "192.168.200.130:9092";
    public static void main(String[] args) throws InterruptedException {


        //添加kafka的配置信息
        Properties properties = new Properties();
        //配置broker信息
        properties.put("bootstrap.servers",KAFKA_IP);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.RETRIES_CONFIG,10);

        for (int i=1;i<100;i++) {
            //生产者对象
            KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);
            //发送消息
            try {
                String msg1 = "c1:2";
                ProducerRecord<String,String> record1 = new ProducerRecord<String, String>(TOPIC,msg1);
                producer.send(record1);

                String msg2 = "c2:1";
                ProducerRecord<String,String> record2 = new ProducerRecord<String, String>(TOPIC,msg2);
                producer.send(record2);
            }catch (Exception e){
                e.printStackTrace();
            }
            //关系消息通道
            producer.close();
            Thread.sleep(2000);
        }


    }
}