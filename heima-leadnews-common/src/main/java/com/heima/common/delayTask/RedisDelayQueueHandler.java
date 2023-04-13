package com.heima.common.delayTask;

public interface RedisDelayQueueHandler<T> {

    /**
     * 执行方法
     * @param t
     */
    void execute(T t);
}
