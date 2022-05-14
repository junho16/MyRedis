package com.nwpu.core.client.impl;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.client.RedisResponseStream;
import com.nwpu.core.command.RedisCommand;
import com.nwpu.core.command.impl.AuthCommand;
import com.nwpu.core.exception.ExceptionThrower;
import com.nwpu.core.server.RedisServer;
import com.nwpu.core.db.RedisDb;

/**
 * @author Junho
 * @date 2022/5/13 22:27
 */
public class RedisClientImpl implements RedisClient {

    protected RedisServer server;

    protected String name;

    protected RedisDb curDb;

    protected RedisResponseStream stream;

    protected boolean auth;

//    protected List<Listener> listeners = new ArrayList<>();

    public RedisClientImpl(final String name, RedisResponseStream stream) {
        this.name = name;
        this.stream = stream;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int flag() {
        return 0;
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {
//        for (Listener listener : listeners) {
//            server().listenerManager().remove(listener);
//        }
    }

    @Override
    public RedisServer server() {
        if (server == null) {
            throw new RuntimeException(this + " 未注册");
        }
        return server;
    }

    @Override
    public void executeCommand(RedisCommand redisCommand) {
        if (!(redisCommand instanceof AuthCommand) && !auth) {
            ExceptionThrower.NO_AUTH.throwException();
        }
        redisCommand.execute();
        server().listenerManager().accept(redisCommand);
    }

    @Override
    public RedisResponseStream stream() {
        return stream;
    }

    @Override
    public RedisDb curDb() {
        if (curDb == null) {
            throw new RuntimeException("暂未设置当前db");
        }
        return curDb;
    }

    @Override
    public DataAccess dataAccess() {
        final RedisClientImpl redisClient = this;
        return new DataAccess() {
            @Override
            public void setCurDb(RedisDb redisDb) {
                redisClient.curDb = redisDb;
            }

            @Override
            public void setServer(RedisServer server) {
                redisClient.server = server;
            }

            @Override
            public void setName(String name) {
                redisClient.name = name;
            }

            @Override
            public void setRedisResponseStream(RedisResponseStream stream) {
                redisClient.stream = stream;
            }

            @Override
            public void setAuth(boolean auth) {
                redisClient.auth = auth;
            }
        };
    }
}
