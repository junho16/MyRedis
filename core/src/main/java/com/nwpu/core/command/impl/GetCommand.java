package com.nwpu.core.command.impl;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.command.AbstractCommand;
import com.nwpu.core.command.RedisCommand;
import com.nwpu.core.command.response.SimpleStringResponse;
import com.nwpu.core.db.RedisDb;
import com.nwpu.core.db.RedisObject;
import com.nwpu.core.db.impl.RedisSds;
import com.nwpu.core.exception.ExceptionThrower;
import com.nwpu.core.util.DBUtil;

/**
 * @author Junho
 * @date 2022/5/14 22:50
 */
public class GetCommand extends AbstractCommand implements RedisCommand {

    private String key;

    public GetCommand(RedisClient redisClient, String key){
        super(redisClient);
        this.key = key;
    }

    @Override
    protected void execute0() {
        exec_get(redisClient , key);
    }

    @Override
    public String decode() {
        return "get " + key;
    }

    public static GetCommand build(RedisClient redisClient, String key) {
        return new GetCommand(redisClient, key);
    }

    private void exec_get(RedisClient redisClient , String key){
        RedisDb db = redisClient.curDb();
        RedisObject value = DBUtil.lookupKeyRead(db, key);
        if (value == null) {
            redisClient.stream().response(SimpleStringResponse.NULL);
            return;
        }
        if (value instanceof RedisSds) {
            redisClient.stream().responseString(value.get().toString());
        } else {
            ExceptionThrower.WRONG_TYPE_OPERATION.throwException();
        }
    }

    @Override
    public String toString() {
        return "GetCommand{" + key + '}';
    }
}
