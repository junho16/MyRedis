package com.nwpu.remote.client;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.client.RedisClientFactory;
import com.nwpu.core.command.CommandBuilder;
import com.nwpu.core.command.RedisCommand;
import com.nwpu.core.server.RedisServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.redis.ArrayRedisMessage;
import io.netty.handler.codec.redis.FullBulkStringRedisMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Junho
 * @date 2022/5/13 14:15
 */
@Slf4j
public class RedisClientHandler extends SimpleChannelInboundHandler<ArrayRedisMessage> {

    private final RedisServer server;

    private RedisClient redisClient;

    public RedisClientHandler(RedisServer redisServer){
        this.server = redisServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ArrayRedisMessage msg) throws Exception {
        System.out.println(msg);
        FullBulkStringRedisMessage[] messages = new FullBulkStringRedisMessage[msg.children().size()];
        for (int i = 0; i < messages.length; i++) {
            messages[i] = (FullBulkStringRedisMessage) msg.children().get(i);
        }
        RedisCommand redisCommand = CommandBuilder.decode(redisClient, messages);
        redisClient.executeCommand(redisCommand);
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        // init redisClient
        redisClient = RedisClientFactory.createRedisClient(ctx);
        redisClient.init();
        server.registerClient(redisClient);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        // destroy redisClient
        redisClient.destroy();
        server.removeClient(redisClient);
    }
}
