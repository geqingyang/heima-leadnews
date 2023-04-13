package com.heima.demo;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.joda.time.DateTime;

import java.time.Duration;
import java.util.Date;
import java.util.Properties;


/**
 * KAFKAstream流处理器
 */
public class CountNumApplication {

    private static final String INPUT_TOPIC = "count-num-stream-topic";
    private static final String OUT_TOPIC = "count-num-stream-consumer";
    private static final String KAFKA_IP = "192.168.200.130:9092";

    public static void main(String[] args) throws InterruptedException {

        Properties prop = new Properties();
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,KAFKA_IP);
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG,"count_num123");
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,Serdes.String().getClass());
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
//        生成流数据
        KStream<String, String> stream = builder.stream(INPUT_TOPIC);
//        接收到的数据是 key=null,value=c1:2 重新构建成  key=c1,value=2 , key=c2,value=3 ,key=c3,value=4

        stream.map(new KeyValueMapper<String, String, KeyValue<String, Integer>>() {
            @Override
            public KeyValue<String, Integer> apply(String key, String value) {
                String[] split = value.split(":");
                return new KeyValue<>(split[0],Integer.valueOf(split[1]));
            }
        }).groupByKey(Grouped.with(Serdes.String(),Serdes.Integer()))
                .windowedBy(TimeWindows.of(Duration.ofSeconds(15)).grace(Duration.ZERO))
                .aggregate(new Initializer<Integer>() {
                    @Override
                    public Integer apply() {
                        System.out.println("==="+ DateTime.now().toString("YYYY-MM-dd HH:mm:ss") +"====初始化==========");
                        return 0;
                    }
                }, new Aggregator<String, Integer, Integer>() {
                    @Override
                    public Integer apply(String s, Integer value, Integer aggregate) {
                        return value+aggregate;
                    }
                },Materialized.with(Serdes.String(),Serdes.Integer()))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream().map(new KeyValueMapper<Windowed<String>, Integer, KeyValue<String, String>>() {
            @Override
            public KeyValue<String, String> apply(Windowed<String> key, Integer value) {
                System.out.println(new Date()+"聚合结果="+key.key()+"----"+value);
                return new KeyValue<>(key.key(),value.toString());
            }
        }).to(OUT_TOPIC);


        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), prop);
        kafkaStreams.start();
        System.out.println("kafkaStream 开启运行");
        Thread.currentThread().join();

    }
}