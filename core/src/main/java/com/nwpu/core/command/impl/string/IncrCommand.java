package com.nwpu.core.command.impl.string;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.command.AbstractCommand;
import com.nwpu.core.command.RedisCommand;
import com.nwpu.core.db.RedisDb;
import com.nwpu.core.db.impl.RedisSds;
import com.nwpu.core.exception.ExceptionThrower;
import com.nwpu.core.util.DBUtil;

/**
 * Redis Incr 命令将 key 中储存的数字值增一。
 * @author 花菜
 * @date 2022/5/16 19:20
 */
public class IncrCommand extends AbstractCommand implements RedisCommand {

    String key;

    protected IncrCommand(RedisClient redisClient , String key) {
        super(redisClient);
        this.key = key;
    }

    public static RedisCommand build(RedisClient redisClient , String key ){
        return new IncrCommand(redisClient , key);
    }

    @Override
    protected void execute0() {
        RedisDb redisDb = redisClient.curDb();
        RedisSds value = DBUtil.lookupKeyStringRead(redisDb , key);
        if(value == null){
            //为空 则应该将其value置为1
            redisDb.dict().set(key , RedisSds.valueOf(1));
            redisClient.stream().responseString("1");
            return;
        }
        Long content = null;
        try{
//            String content = value.content();
            content = Long.parseLong(value.content());
        }catch (NumberFormatException e){
            ExceptionThrower. NOT_INTEGER_OR_OUT_OF_RANGE.throwException();
        }
        if(content + 1 > Long.MAX_VALUE ){
            ExceptionThrower.NOT_INTEGER_OR_OUT_OF_RANGE.throwException();
        }else{
            redisDb.dict().set(key , RedisSds.valueOf(++content));
        }
        redisClient.stream().response(content);

    }

    @Override
    public String decode() {
        return "incr " + key;
    }

}
