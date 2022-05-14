package com.nwpu.core.db;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/5/13 16:08
 */
public interface RedisDb {

    int id();

    /**
     * 此处应该是string map等都有可能 即 Type
     * @return
     */
//    ConcurrentHashMap<String, T> dict();
//
//    ConcurrentHashMap<String, Long> expires();

    Dict<String, RedisObject> dict();

    Dict<String, Long> expires();
}
