package com.nwpu.core.db.impl;

import com.nwpu.core.db.Dict;
import com.nwpu.core.db.RedisDb;
import com.nwpu.core.db.RedisObject;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Junho
 * @date 2022/5/13 22:04
 */
public class RedisDbImpl implements RedisDb {

    private final int id;

    private Dict dict = new RedisDict();

    private Dict expires = new RedisDict();

    public RedisDbImpl(final int id) {
        this.id = id;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public Dict<String, RedisObject> dict() {
        return dict;
    }

    @Override
    public Dict<String, Long> expires() {
        return expires;
    }

}
