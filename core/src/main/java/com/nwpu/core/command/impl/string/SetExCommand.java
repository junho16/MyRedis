package com.nwpu.core.command.impl.string;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.command.AbstractCommand;
import com.nwpu.core.command.RedisCommand;
import com.nwpu.core.command.response.SimpleStringResponse;
import com.nwpu.core.db.RedisDb;
import com.nwpu.core.db.impl.RedisSds;
import com.nwpu.core.exception.ExceptionThrower;
import com.nwpu.core.util.TimeUtil;

/**
 * @author Junho
 * @date 2022/5/15 16:40
 */
public class SetExCommand extends AbstractCommand implements RedisCommand {

    private String key;

    private String value;

    private long expireSec;

    public SetExCommand(RedisClient redisClient, String key, String value, long expireSec) {
        super(redisClient);
        this.key = key;
        this.value = value;
        this.expireSec = expireSec;
    }

    @Override
    public void execute0() {
        if (expireSec <= 0) {
            ExceptionThrower.INVALID_EXPIRE_TIME.throwException("setex");
        }
        RedisDb db = redisClient.curDb();
        db.dict().set(key, RedisSds.valueOf(value));
        db.expires().set(key, TimeUtil.nextSecTimeMillis(expireSec));
        redisClient.stream().response(SimpleStringResponse.OK);
    }

    @Override
    public String decode() {
        return String.format("setex %s %d %s", key, expireSec, value);
    }


    public static SetCommand build(RedisClient redisClient, String key, String value, long expireSec) {
        return new SetCommand(redisClient, key, value, expireSec);
    }
}
