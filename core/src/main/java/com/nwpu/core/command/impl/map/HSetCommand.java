package com.nwpu.core.command.impl.map;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.command.AbstractCommand;
import com.nwpu.core.command.RedisCommand;
import com.nwpu.core.command.response.SimpleStringResponse;
import com.nwpu.core.db.RedisDb;
import com.nwpu.core.db.RedisObject;
import com.nwpu.core.db.impl.RedisDict;
import com.nwpu.core.db.impl.RedisSds;
import com.nwpu.core.exception.ExceptionThrower;
import com.nwpu.core.util.DBUtil;

import java.util.HashMap;

/**
 * @author 花菜
 * @date 2022/5/16 20:10
 */
public class HSetCommand extends AbstractCommand implements RedisCommand {

    private String key;

    private String field;

    private String value;

    public static HSetCommand build(RedisClient redisClient , String key , String field , String value ){
        return new HSetCommand(redisClient , key , field , value);
    }

    protected HSetCommand(RedisClient redisClient , String key , String field , String value ){
        super(redisClient);
        this.key = key;
        this.field = field;
        this.value = value;
    }

    @Override
    protected void execute0() {
        RedisDb db = redisClient.curDb();
        RedisDict<String, RedisSds> baseDict = getAndInitHMap(db, key);
        baseDict.set(field, RedisSds.valueOf(value));
        redisClient.stream().response(SimpleStringResponse.OK);
    }

    /**
     * 有则取 没有则新建
     * @param db
     * @param key
     * @return
     */
    private RedisDict<String, RedisSds> getAndInitHMap(RedisDb db, String key) {
        RedisObject hMap = DBUtil.lookupKeyRead(db, key);
        if (hMap == null) {
            synchronized (db) {
                //此处加锁的时候需要再判断一下，以保证其不会出现 某个线程操作导致其存在 的情况
                hMap = db.dict().get(key);
                if (hMap == null) {
                    hMap = new RedisDict();
                    db.dict().set(key, hMap);
                }
            }
        }
        if (!(hMap instanceof RedisDict)) {
            ExceptionThrower.WRONG_TYPE_OPERATION.throwException();
        }
        return (RedisDict<String, RedisSds>) hMap;
    }

    @Override
    public String decode() {
        return "hset " + key + " " + field + " " + value;
    }
}
