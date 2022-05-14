package com.nwpu.remote;

import lombok.extern.slf4j.Slf4j;

/**
 * 通信启动类
 * @author Junho
 * @date 2022/5/13 14:13
 */
@Slf4j
public class MyRedisApplication {

    private static final boolean SSL = System.getProperty("ssl") != null;
    
    public static void main(String[] args) {
        long start = System.currentTimeMillis();





        long end = System.currentTimeMillis();
        log.info("redis已启动，耗时：{}" , end - start);
    }
}
