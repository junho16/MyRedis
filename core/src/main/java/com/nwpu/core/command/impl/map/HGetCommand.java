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
import io.netty.handler.codec.redis.RedisBulkStringAggregator;

/**
 * @author 花菜
 * @date 2022/5/17 11:01
 */
public class HGetCommand extends AbstractCommand implements RedisCommand {

    private String key;

    private String field;

    protected HGetCommand(RedisClient redisClient , String key , String field) {
        super(redisClient);
        this.key = key;
        this.field = field;
    }

    public static HGetCommand build(RedisClient redisClient , String key , String field){
        return new HGetCommand(redisClient , key , field);
    }

    @Override
    protected void execute0() {
        RedisDb db = redisClient.curDb();
        RedisObject hMap = DBUtil.lookupKeyRead(db, key);
        if(hMap == null){
            redisClient.stream().response(SimpleStringResponse.NULL);
            return;
        }else{
            if (!(hMap instanceof RedisDict)) {
                ExceptionThrower.WRONG_TYPE_OPERATION.throwException();
            }else{
                RedisDict<String, RedisSds> dict = (RedisDict) hMap;
                RedisSds value = dict.get(field);
                if(value == null){
                    redisClient.stream().response(SimpleStringResponse.NULL);
                }else{
                    redisClient.stream().responseString(value.get());
                }
            }
        }
    }

    @Override
    public String decode() {
        return "hget " + key + " " + field;
    }
}
