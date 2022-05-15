package com.nwpu.core.command.impl;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.command.AbstractCommand;
import com.nwpu.core.command.RedisCommand;
import com.nwpu.core.command.response.SimpleStringResponse;
import com.nwpu.core.listen.impl.AbstractListener;

import java.sql.ClientInfoStatus;

/**
 * @author Junho
 * @date 2022/5/15 14:13
 */
public class MonitorCommand extends AbstractCommand implements RedisCommand {

    @Override
    protected void execute0() {
        redisClient.addListener(new AbstractListener<RedisCommand>() {
            @Override
            public void accept0(RedisCommand event) {
                redisClient.stream().responseString(event.decode());
            }
        });
        redisClient.stream().response(SimpleStringResponse.OK);
    }

    @Override
    public String decode() {
        return "monitor";
    }

    protected MonitorCommand(RedisClient redisClient) {
        super(redisClient);
    }

    public static MonitorCommand build(RedisClient redisClients){
        return new MonitorCommand(redisClients);
    }
}
