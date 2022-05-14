package com.nwpu.core.db;

/**
 * @author Junho
 * @date 2022/5/14 21:22
 */
public interface RedisObject<V> {

    Type type();

    V get();
}
