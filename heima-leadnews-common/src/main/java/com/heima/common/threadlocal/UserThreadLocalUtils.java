package com.heima.common.threadlocal;

/**
 * 存放用户id的容器
 */
public class UserThreadLocalUtils {

    private final static ThreadLocal<Integer> userThreadLocal = new ThreadLocal<>();

    /**
     * 获取线程中的用户
     *
     * @return
     */
    public static Integer getUserId() {
        return userThreadLocal.get();
    }

    /**
     * 设置当前线程中的用户
     *
     * @param userId
     */
    public static void setUserId(Integer userId) {
        userThreadLocal.set(userId);
    }

    public static void removeUser(){userThreadLocal.remove();}

}
