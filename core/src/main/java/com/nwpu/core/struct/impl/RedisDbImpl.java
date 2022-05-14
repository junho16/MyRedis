package com.nwpu.core.struct.impl;

import com.nwpu.core.struct.RedisDb;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/5/13 22:04
 */
public class RedisDbImpl implements RedisDb {

    private final int id;

    private ConcurrentHashMap dict = new ConcurrentHashMap();

    private ConcurrentHashMap expires = new ConcurrentHashMap();

    public RedisDbImpl(final int id) {
        this.id = id;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public ConcurrentHashMap dict() {
        return dict;
    }

    @Override
    public ConcurrentHashMap<String, Long> expires() {
        return expires;
    }
}
