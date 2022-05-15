package com.nwpu.core.command.impl;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.command.AbstractCommand;
import com.nwpu.core.command.RedisCommand;
import com.nwpu.core.command.response.SimpleStringResponse;
import com.nwpu.core.server.RedisServer;

/**
 * 切换到指定的数据库
 * @author Junho
 * @date 2022/5/15 15:44
 */
public class SelectDbCommand extends AbstractCommand implements RedisCommand {

    private int dbIndex;

    public static SelectDbCommand build(RedisClient redisClient , int dbIndex){
        return new SelectDbCommand(redisClient , dbIndex);
    }

    protected SelectDbCommand(RedisClient redisClient , int dbIndex) {
        super(redisClient);
        this.dbIndex = dbIndex;
    }

    @Override
    protected void execute0() {
        RedisServer redisServer = redisClient.server();
        redisClient.dataAccess().setCurDb(redisServer.db(dbIndex));
        redisClient.stream().response(SimpleStringResponse.OK);
    }

    @Override
    public String decode() {
        return "select " + dbIndex;
    }
}
