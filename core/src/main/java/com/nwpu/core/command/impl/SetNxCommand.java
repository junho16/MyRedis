package com.nwpu.core.command.impl;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.command.AbstractCommand;
import com.nwpu.core.command.RedisCommand;
import com.nwpu.core.db.RedisDb;
import com.nwpu.core.db.RedisObject;
import com.nwpu.core.db.impl.RedisSds;
import com.nwpu.core.util.DBUtil;

/**
 * set 需要保证 数据库中没有此key的记录，即key不存在时才可以set
 * 否则set不覆盖原有的值
 * @author Junho
 * @date 2022/5/15 16:09
 */
public class SetNxCommand extends AbstractCommand implements RedisCommand {

    private String key;

    private String value;

    protected SetNxCommand(RedisClient redisClient , String key , String value) {
        super(redisClient);
        this.key = key;
        this.value = value;
    }

    public static SetNxCommand build(RedisClient redisClient , String key, String value) {
        return new SetNxCommand(redisClient , key , value);
    }
    
    @Override
    protected void execute0() {
        RedisDb db = redisClient.curDb();
        RedisObject oldValue = DBUtil.lookupKeyRead(db, key);
        if (oldValue == null) {
            db.dict().setnx(key, RedisSds.valueOf(value));
            redisClient.stream().responseString("1");
        } else {
            redisClient.stream().responseString("0");
        }
    }

    @Override
    public String decode() {
        return String.format("setnx %s %s", key, value);
    }
}
