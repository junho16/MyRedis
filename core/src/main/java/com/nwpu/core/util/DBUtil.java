package com.nwpu.core.util;

import com.nwpu.core.db.RedisDb;
import com.nwpu.core.db.RedisObject;
import com.nwpu.core.db.impl.RedisSds;
import com.nwpu.core.exception.ExceptionThrower;

/**
 * @author Junho
 * @date 2022/5/14 23:06
 */
public class DBUtil {

    public static RedisObject lookupKeyRead(RedisDb db, String key) {
        if (expireIfNeeded(db, key)) {
            // 已过期
            return null;
        }
        return db.dict().get(key);
    }


    public static RedisSds lookupKeyStringRead(RedisDb db, String key) {
        if (expireIfNeeded(db, key)) {
            return null;
        }
        RedisObject object = db.dict().get(key);
        if (object == null) {
            return null;
        }
        if (!(object instanceof RedisSds)) {
            ExceptionThrower.WRONG_TYPE_OPERATION.throwException();
        }
        return (RedisSds) object;
    }

    public static RedisObject delKey(RedisDb db, String key) {
        if (expireIfNeeded(db, key)) {
            return null;
        }
        RedisObject o = db.dict().remove(key);
        if (o != null) {
            db.expires().remove(key);
        }
        return o;
    }

    /**
     * 过期返回true，并将 过期时间 与 已过期的KV值 删除
     * 不过期返回false
     * @param db
     * @param key
     * @return
     */
    private static final boolean expireIfNeeded(RedisDb db, String key) {
        Long ttl = db.expires().get(key);
        if (!isLive(ttl)) {
            //已过期
            db.expires().remove(key);
            db.dict().remove(key);
            return true;
        }
        return false;
    }


    /**
     * 判断是否已经过期
     * 过期：eturn false
     * 未设置过期时间 或者 不过期：return true
     * @param ttl
     * @return
     */
    private static boolean isLive(Long ttl) {
        return ttl == null || ttl.longValue() > TimeUtil.currentTimeMillis();
    }
}
