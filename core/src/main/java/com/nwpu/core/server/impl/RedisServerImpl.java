package com.nwpu.core.server.impl;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.listen.ListenerManager;
import com.nwpu.core.listen.impl.CommandListener;
import com.nwpu.core.listen.impl.ListenerManagerImpl;
import com.nwpu.core.server.RedisInfo;
import com.nwpu.core.server.RedisServer;
import com.nwpu.core.server.entity.Logo;
import com.nwpu.core.server.entity.RedisConfig;
import com.nwpu.core.struct.RedisDb;
import com.nwpu.core.struct.impl.RedisDbImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Junho
 * @date 2022/5/13 16:09
 */
@Slf4j
@Data
public class RedisServerImpl implements RedisServer {

    /**
     * 保证初始化的原子性
     */
    private AtomicBoolean initFlag = new AtomicBoolean(false);

    private final Map<String, RedisClient> clients = new ConcurrentHashMap<>();

    private final ListenerManager listenerManager = new ListenerManagerImpl();

    private final BasicRedisConfig redisConfig = new BasicRedisConfig();

    private RedisDb[] dbs;

    private AtomicBoolean init = new AtomicBoolean(false);

    public RedisServerImpl(){
        listenerManager.addListener(new CommandListener());
    }

    @Override
    public void init() {
        if(!initFlag.compareAndSet(false , true)){
            log.info("RedisServer已初始化！");
        }else{
            System.out.println(Logo.ASCII_LOGO);
            //初始化数据库 db
            dbs = new RedisDb[redisConfig.getDbNum()];
            for (int i = 0; i < dbs.length; i++) {
                dbs[i] = new RedisDbImpl(i);
            }
        }
    }

    @Override
    public RedisInfo getRedisInfo() {
        return null;
    }

    @Override
    public void registerClient(RedisClient redisClient) {
        clients.put(redisClient.name(), redisClient);
        RedisClient.DataAccess dataAccess = redisClient.dataAccess();
        dataAccess.setServer(this);
        dataAccess.setCurDb(dbs[0]);
        dataAccess.setAuth(redisConfig.getPassword() == null);
    }

    @Override
    public void removeClient(RedisClient redisClient) {
        clients.remove(redisClient.name());
        RedisClient.DataAccess dataAccess = redisClient.dataAccess();
        dataAccess.setServer(null);
        dataAccess.setCurDb(null);
    }

    @Override
    public void destroy() {
//        clients.values().forEach(x -> registerClient(x));
        clients.values().forEach(x -> removeClient(x));
        listenerManager.removeAll();
        dbs = null;
    }

    @Override
    public RedisDb db(int index) {
        return dbs[index];
    }

    @Override
    public boolean auth(String password) {
        return redisConfig.getPassword().equals(password);
    }

    @Override
    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    @Override
    public ListenerManager listenerManager() {
        return listenerManager;
    }

    /**
     * 继承抽象类，保证其 init 在 set 之前，set需要保证未init Server
     */
    class BasicRedisConfig extends RedisConfig {

        @Override
        public void setHost(String host) {
            verifyStatus();
            this.host = host;
        }

        @Override
        public void setPort(int port) {
            verifyStatus();
            this.port = port;
        }

        @Override
        public void setDbNum(int dbNum) {
            verifyStatus();
            this.dbNum = dbNum;
        }

        @Override
        public void setPassword(String password) {
            verifyStatus();
            this.password = password;
        }

        private void verifyStatus() {
            if (!initFlag.get()) {
                throw new RuntimeException("已初始化RedisServer，不可修改config属性！");
            }
        }
    }

}
