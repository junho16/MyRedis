package com.nwpu.core.command;

import com.nwpu.core.client.RedisClient;

/**
 * @author Junho
 * @date 2022/5/14 13:24
 */
public abstract class AbstractCommand implements RedisCommand {

    protected final RedisClient redisClient;

    protected long cost;

    protected AbstractCommand(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public void execute() {
        long start = System.nanoTime();
        execute0();
        cost = System.nanoTime() - start;
    }

    protected abstract void execute0();
}
