package com.nwpu.core.command.impl;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.command.AbstractCommand;
import com.nwpu.core.command.RedisCommand;
import com.nwpu.core.db.RedisDb;
import com.nwpu.core.util.DBUtil;

import java.util.Arrays;

/**
 * @author Junho
 * @date 2022/5/15 16:43
 */
public class DelCommand extends AbstractCommand implements RedisCommand {

    private final String[] keys;

    public DelCommand(RedisClient redisClient, String... keys) {
        super(redisClient);
        this.keys = keys;
    }

    @Override
    public void execute0() {
        RedisDb redisDb = redisClient.curDb();
        int res = 0;
        for (String key : keys) {
            if (DBUtil.delKey(redisDb, key) != null) {
                res++;
            }
        }
        redisClient.stream().responseString(String.valueOf(res));
    }

    @Override
    public String decode() {
        return "del " + Arrays.toString(keys);
    }


    public static DelCommand build(RedisClient redisClient, String... keys) {
        return new DelCommand(redisClient, keys);
    }
}
