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

/**
 * @author 花菜
 * @date 2022/5/17 14:31
 */
public class HDelCommand extends AbstractCommand implements RedisCommand {

    private String key;

    private String field;

    public static HDelCommand build(RedisClient redisClient, String key , String field){
        return new HDelCommand(redisClient , key , field);
    }

    protected HDelCommand(RedisClient redisClient , String key , String field) {
        super(redisClient);
        this.key = key;
        this.field = field;
    }

    @Override
    protected void execute0() {
//        RedisDb db = redisClient.curDb();
//        RedisObject hMap = DBUtil.lookupKeyRead(db, key);
//        if (hMap == null) {
//            redisClient.stream().response(SimpleStringResponse._0);
//            return;
//        }
//        if (!(hMap instanceof BaseDict)) {
//            ExceptionThrower.WRONG_TYPE_OPERATION.throwException();
//        } else {
//            BaseDict<String, Sds> dict = (BaseDict) hMap;
//            Sds value = dict.remove(field);
//            if (value == null) {
//                redisClient.stream().response(SimpleStringResponse._0);
//            } else {
//                redisClient.stream().response(SimpleStringResponse._1);
//            }
//            if (dict.size() == 0) {
//                db.dict().remove(key);
//            }
//        }

        RedisDb redisDb = redisClient.curDb();
        RedisObject value = DBUtil.lookupKeyRead(redisDb , key);
        if(value == null){
            redisClient.stream().response(SimpleStringResponse._0);
            return;
        }else{
//            System.out.println(value.getClass().toString());
            if(value instanceof RedisDict){
                //确保类型正确
                RedisDict<String , RedisSds> dict = (RedisDict) value;
                RedisSds sds = dict.remove(field);
                if(sds == null){
                    redisClient.stream().response(SimpleStringResponse._0);
                }else{
                    redisClient.stream().response(SimpleStringResponse._1);
                }
                //若map的最后一个元素删除完毕，则直接将key value直接删去
                if(dict.size() == 0){
                    redisDb.dict().remove(key);
                }
            }else{
                ExceptionThrower.WRONG_TYPE_OPERATION.throwException();
            }
        }
    }

    @Override
    public String decode() {
        return "hdel " + key + " " + field;
    }
}
