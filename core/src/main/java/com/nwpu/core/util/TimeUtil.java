package com.nwpu.core.util;

/**
 * @author Junho
 * @date 2022/5/14 21:41
 */
public class TimeUtil {

    public static long currentTimeSec() {
        return currentTimeMillis() / 1000L;
    }


    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }


    public static long nextSecTimeMillis(long sec) {
        return nextMillisTimeMillis(sec * 1000);
    }

    public static long nextMillisTimeMillis(long millis) {
        return currentTimeMillis() + millis;
    }
}
