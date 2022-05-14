package com.nwpu.core.db;

/**
 * Redis底层Map的数据结构 接口
 *
 * @author Junho
 * @date 2022/5/14 21:14
 */
public interface Dict<K , V> {

    V get(K key);

    void set(K key, V value);

    V setnx(K key, V value);

    boolean exist(K key);

    V remove(K key);

    long size();
}
