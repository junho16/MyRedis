package com.nwpu.core.server;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.listen.ListenerManager;
import com.nwpu.core.server.entity.RedisConfig;
import com.nwpu.core.struct.RedisDb;

/**
 * 服务端接口
 * @author Junho
 * @date 2022/5/13 15:55
 */
public interface RedisServer {

    /**
     * 初始化
     */
    void init();

    /**
     * 获取Redis的相关信息
     * @return
     */
    RedisInfo getRedisInfo();

    /**
     * 注册客户端
     */
    void registerClient(RedisClient redisClient);

    /**
     * 移除客户端
     */
    void removeClient(RedisClient redisClient);

    /**
     * 注销
     */
    void destroy();

    /**
     * 获得指定位置的DB
     * @param index
     * @return
     */
    RedisDb db(int index);

    /**
     * 鉴权
     * @param password
     * @return
     */
    boolean auth(String password);

    /**
     * 获得对于客户端的监听
     * @return
     */
    ListenerManager listenerManager();

    RedisConfig getRedisConfig();
}
