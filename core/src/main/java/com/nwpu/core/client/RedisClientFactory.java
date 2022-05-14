package com.nwpu.core.client;

import com.nwpu.core.client.impl.RedisClientImpl;
import com.nwpu.remote.client.RemoteRedisResponseStream;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Junho
 * @date 2022/5/14 11:35
 */
public class RedisClientFactory {

    private static final AtomicLong COUNTER = new AtomicLong(0);

    public static RedisClient createRedisClient(ChannelHandlerContext ctx) {
        return new RedisClientImpl(newName(), new RemoteRedisResponseStream(ctx));
    }

    private static String newName() {
        return "client-" + COUNTER.getAndIncrement();
    }

}
