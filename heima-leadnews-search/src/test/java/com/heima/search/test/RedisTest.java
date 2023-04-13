package com.heima.search.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTest {

//    @Autowired
//    private RedissonClient client;
//    @Test
//    public void testBloom(){
//
//        RBloomFilter<Object> bloomFilter = client.getBloomFilter("bloom");
//
//        bloomFilter.tryInit(100000000L,0.01);
//
//        bloomFilter.add("1");
//
//        bloomFilter.contains("2");
//
//    }
}
