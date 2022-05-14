package com.nwpu.core.command.impl;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.command.AbstractCommand;
import com.nwpu.core.command.RedisCommand;
import com.nwpu.core.command.response.SimpleStringResponse;
import com.nwpu.core.db.impl.RedisSds;
import com.nwpu.core.exception.ExceptionThrower;
import com.nwpu.core.db.RedisDb;
import com.nwpu.core.util.TimeUtil;

/**
 * set操作 需要注意 set有一些操作 (目前只先实现一个setex和set)
 * EX (SETEX key seconds value) 将 key的过期时间设置为 seconds（SETEX key seconds value）
 *
 * NX (SETNX key value) 只有在 key 不存在时设置 key 的值。
 * RANGE (SETRANGE key offset value) 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始。
 * BIT (SETBIT key offset value) 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)。
 *
 * @author Junho
 * @date 2022/5/14 20:21
 */
public class SetCommand extends AbstractCommand implements RedisCommand {

    private String key;

    private String value;

    private Long expireSec;

    protected SetCommand(RedisClient redisClient, String key, String value, Long expireSec) {
        super(redisClient);
        this.key = key;
        this.value = value;
        this.expireSec = expireSec;
    }

    @Override
    protected void execute0() {
        if (expireSec != null && expireSec <= 0) {
            ExceptionThrower.INVALID_EXPIRE_TIME.throwException("set");
        }
        //此处使用的是默认的server的dbs（dbs[0]）
        RedisDb db = redisClient.curDb();
        db.dict().set(key, RedisSds.valueOf(value));
        if (expireSec != null) {
            //换成ms
            db.expires().set(key, TimeUtil.nextSecTimeMillis(expireSec));
        }
        redisClient.stream().response(SimpleStringResponse.OK);
    }

    @Override
    public String decode() {
        if (expireSec != null) {
            return String.format("set %s ex %s %d", key, value, expireSec);
        }
        return String.format("set %s %s", key, value);
    }

    public static SetCommand build(RedisClient redisClient, String key, String value, Long expireSec) {
        return new SetCommand(redisClient, key, value, expireSec);
    }
}
