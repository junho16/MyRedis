package com.nwpu.core.command.response;

import com.nwpu.core.client.RedisResponse;

/**
 * @author Junho
 * @date 2022/5/14 12:32
 */
public class SimpleStringResponse implements RedisResponse {
    public static final RedisResponse _0 = new SimpleStringResponse("0");

    public static final RedisResponse _1 = new SimpleStringResponse("1");

    public static final RedisResponse OK = new SimpleStringResponse("OK");

    public static final RedisResponse NULL = new SimpleStringResponse("(nil)");

    protected final String content;

    private SimpleStringResponse(String content) {
        this.content = content;
    }

    public static SimpleStringResponse valueOf(String value) {
        return new SimpleStringResponse(value);
    }

    @Override
    public String decode() {
        return content;
    }

}
