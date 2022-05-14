package com.nwpu.core.command.response;

import com.nwpu.core.client.RedisResponse;

import java.util.List;

/**
 * @author Junho
 * @date 2022/5/14 12:33
 */
public class ArrayResponse implements RedisResponse {

    private List<RedisResponse> redisResponses;

    public ArrayResponse(List<RedisResponse> redisResponses) {
        this.redisResponses = redisResponses;
    }

    public List<RedisResponse> getRedisResponses() {
        return redisResponses;
    }

    public void setRedisResponses(List<RedisResponse> redisResponses) {
        this.redisResponses = redisResponses;
    }

    @Override
    public String decode() {
        return redisResponses.toString();
    }
}
