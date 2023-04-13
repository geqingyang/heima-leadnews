package com.heima.admin;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class test{
    @Test
    public void threadTest(){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,5,30, TimeUnit.SECONDS
        ,BlockingQueue);
    }

}
class MyThread extends Thread{
    @Override
    public void run() {
        super.run();
    }
}
class MyThread2 implements Runnable{

    @Override
    public void run() {


    }
}
class MyThread3 implements Callable{

    @Override
    public Object call() throws Exception {
        return null;
    }
}
