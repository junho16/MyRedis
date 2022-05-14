package com.nwpu.core.db.impl;

import com.nwpu.core.db.RedisObject;
import com.nwpu.core.db.Type;

import java.util.Objects;

/**
 * Redis底层使用的是自己定义的动态字符串
 *
 * @author Junho
 * @date 2022/5/14 21:19
 */
public class RedisSds implements RedisObject<String> {

    public static final RedisSds valueOf(String content) {
        return new RedisSds(content);
    }

    public static final RedisSds valueOf(Number number) {
        return new RedisSds(number);
    }

    private final String content;

    public RedisSds(String content) {
        this.content = content;
    }

    public RedisSds(Number number) {
        this.content = number.toString();
    }

    public String content() {
        return content;
    }

    @Override
    public Type type() {
        return Type.string;
    }

    @Override
    public String get() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RedisSds that = (RedisSds) o;
        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
