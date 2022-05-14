package com.nwpu.remote.client;

import com.nwpu.core.client.RedisResponse;
import com.nwpu.core.client.RedisResponseStream;
import com.nwpu.core.command.response.ArrayResponse;
import com.nwpu.core.command.response.SimpleStringResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.redis.ArrayRedisMessage;
import io.netty.handler.codec.redis.ErrorRedisMessage;
import io.netty.handler.codec.redis.RedisMessage;
import io.netty.handler.codec.redis.SimpleStringRedisMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Junho
 * @date 2022/5/14 11:40
 */
public class RemoteRedisResponseStream implements RedisResponseStream {

    private final ChannelHandlerContext ctx;

    public RemoteRedisResponseStream(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void response(RedisResponse msg) {
        if (msg instanceof SimpleStringResponse) {
            responseString(msg.decode());
        } else if (msg instanceof ArrayResponse) {
            ArrayResponse tmp = (ArrayResponse) msg;
            List<RedisMessage> redisMessages = new ArrayList<>(tmp.getRedisResponses().size());
            for (RedisResponse redisResponse : tmp.getRedisResponses()) {
                redisMessages.add(new SimpleStringRedisMessage(redisResponse.decode()));
            }
            ctx.write(new ArrayRedisMessage(redisMessages));
            ctx.flush();
        }
    }

    @Override
    public void responseString(String str) {
        ctx.write(new SimpleStringRedisMessage(str));
        ctx.flush();
    }

    @Override
    public void response(Object object) {
        responseString(object.toString());
    }


    @Override
    public void error(String error) {
        ctx.write(new ErrorRedisMessage(error));
        ctx.flush();
    }

}
