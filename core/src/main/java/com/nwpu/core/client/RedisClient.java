package com.nwpu.core.client;

import com.nwpu.core.command.RedisCommand;
import com.nwpu.core.listen.Listener;
import com.nwpu.core.server.RedisServer;
import com.nwpu.core.db.RedisDb;

/**
 * @author Junho
 * @date 2022/5/13 22:21
 */
public interface RedisClient {

    String name();

    int flag();

    void init();

    void destroy();

    RedisServer server();

    void executeCommand(RedisCommand redisCommand);

    RedisResponseStream stream();

    RedisDb curDb();

    DataAccess dataAccess();

//    RedisCommand getCache(String command);

    void addListener(Listener listener);

    /**
     * 不使用抽象类 则使用内嵌接口
     */
    interface DataAccess {

        void setCurDb(RedisDb redisDb);

        void setServer(RedisServer server);

        void setName(String name);

        void setRedisResponseStream(RedisResponseStream stream);

        void setAuth(boolean auth);
    }
}
