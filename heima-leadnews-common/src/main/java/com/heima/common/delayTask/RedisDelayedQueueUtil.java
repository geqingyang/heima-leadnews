package com.heima.common.delayTask;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisDelayedQueueUtil {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 添加延迟队列
     * @param t
     * @param delay
     * @param timeUnit
     * @param queueName
     * @param <T>
     */
    public <T> void addQueue(T t, long delay, TimeUnit timeUnit,String queueName){
        RBlockingDeque<T> blockingDeque = redissonClient.getBlockingDeque(queueName);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);

        delayedQueue.offer(t,delay,timeUnit);
        log.info("(添加延时队列成功) 队列键：{}，队列值：{}，延迟时间：{}",queueName,t,delay);
    }

    public <T> T getDelayQueue(String queueName) throws InterruptedException {
        RBlockingDeque<T> blockingDeque = redissonClient.getBlockingDeque(queueName);
        T take = blockingDeque.take();
        return take;
    }

    public <T> void removeDelayQueue(String queueName,T t) {
        RBlockingDeque<T> blockingDeque = redissonClient.getBlockingDeque(queueName);
        RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        boolean remove = delayedQueue.remove(t);
        log.info("删除t={},remove=={}",t,remove);
    }
}
