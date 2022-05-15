package com.nwpu.core.command;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.command.impl.*;
import com.nwpu.core.exception.ExceptionThrower;
import com.nwpu.core.exception.RedisException;
import io.netty.handler.codec.CodecException;
import io.netty.handler.codec.redis.*;
import io.netty.util.CharsetUtil;

/**
 * 服务端收到消息后构造请求
 * @author Junho
 * @date 2022/5/14 12:48
 */
public class CommandBuilder {

    /**
     * @param redisClient
     * @param messages
     * @return
     */
    public static final RedisCommand decode(RedisClient redisClient, FullBulkStringRedisMessage[] messages) {
        if (messages == null || messages.length == 0) {
            return null;
        }
        String[] decodes = new String[messages.length];
        for (int i = 0; i < messages.length; i++) {
            decodes[i] = decodeMessageToString(messages[i]);
        }
        for(String s : decodes){
            System.out.println(s);
        }
        System.out.println();

        return decode(redisClient, decodes);
    }


    /**
     * @param redisClient
     * @param messages
     * @return
     */
    public static final RedisCommand decode(RedisClient redisClient, String[] messages) {
        String commandTag = "unknown";
        try {
            commandTag = messages[0].toLowerCase();
            switch (commandTag) {
                case CommandType.AUTH:
                    return AuthCommand.build(redisClient, messages[1]);
                case CommandType.SELECT:
                    return SelectDbCommand.build(redisClient, Integer.parseInt(messages[1]));
                case CommandType.MONITOR:
                    return MonitorCommand.build(redisClient);
                case CommandType.GET:
                    return GetCommand.build(redisClient, messages[1]);
                case CommandType.SET:
                    return tryBuildSetCommand(redisClient, messages);
                case CommandType.SETNX:
                    return SetNxCommand.build(redisClient, messages[1], messages[2]);
                case CommandType.SETEX:
                    return SetExCommand.build(redisClient, messages[1], messages[3], Long.parseLong(messages[2]));
//                case CommandType.PSETEX:
//                    return PSetExCommand.build(redisClient, messages[1], messages[3], Long.parseLong(messages[2]));
//                case CommandType.STR_LEN:
//                    return StrLenCommand.build(redisClient, messages[1]);
                case CommandType.DEL:
                    return tryBuildDelCommand(redisClient, messages);
//                case CommandType.EXPIRE:
//                    return ExpireCommand.build(redisClient, messages[1], Long.parseLong(messages[2]));
//                case CommandType.TTL:
//                    return TTLCommand.build(redisClient, messages[1]);
//                case CommandType.PTTL:
//                    return PTTLCommand.build(redisClient, messages[1]);
//                case CommandType.INCR:
//                    return IncrCommand.build(redisClient, messages[1]);
//                case CommandType.DECR:
//                    return DecrCommand.build(redisClient, messages[1]);
//                case CommandType.TYPE:
//                    return TypeCommand.build(redisClient, messages[1]);
//                case CommandType.MGET:
//                    return MGetCommand.build(redisClient, messages);
//                case CommandType.HDEL:
//                    return HDelCommand.build(redisClient, messages[1], messages[2]);
//                case CommandType.HGET:
//                    return HGetCommand.build(redisClient, messages[1], messages[2]);
//                case CommandType.HSET:
//                    return HSetCommand.build(redisClient, messages[1], messages[2], messages[3]);
                default:
                    ExceptionThrower.UNKNOWN_COMMAND.throwException(commandTag);
            }
        } catch (Exception ex) {
            if (ex instanceof RedisException) {
                throw ex;
            }
            ExceptionThrower.ERROR_PARAM.throwException(commandTag);
        }
        return null;
    }


    /**
     * 此 set 操作 （set k v | set k v ex seconds）
     * 但是不是按照redis的规则来的，实际上应该是：
     *（SET key value [NX] [XX] [EX <seconds>] [PX <milliseconds>]）
     *（EX与PX互斥 NX与XX互斥）
     * @param redisClient
     * @param messages
     * @return
     */
    private static SetCommand tryBuildSetCommand(RedisClient redisClient, String[] messages) {
        if (messages.length == 3) {
            return SetCommand.build(redisClient, messages[1], messages[2], null);
        }
        if (messages.length == 5 && "ex".equalsIgnoreCase(messages[3])) {
            return SetCommand.build(redisClient, messages[1], messages[2], Long.parseLong(messages[4]));
        }
        ExceptionThrower.ERROR_PARAM.throwException("set");
        return null;
    }

    private static DelCommand tryBuildDelCommand(RedisClient redisClient, String[] messages) {
        String[] keys = new String[messages.length - 1];
        for (int i = 1; i < messages.length; i++) {
            keys[i - 1] = messages[i];
        }
        return DelCommand.build(redisClient, keys);
    }


    private static String decodeMessageToString(RedisMessage msg) {

        if (msg instanceof SimpleStringRedisMessage) {
            return ((SimpleStringRedisMessage) msg).content();
        } else if (msg instanceof ErrorRedisMessage) {
            return ((ErrorRedisMessage) msg).content();
        } else if (msg instanceof IntegerRedisMessage) {
            return String.valueOf(((IntegerRedisMessage) msg).value());
        } else if (msg instanceof FullBulkStringRedisMessage) {
            return ((FullBulkStringRedisMessage) msg).content().toString(CharsetUtil.UTF_8);
        }
        throw new CodecException("unknown message type: " + msg);
    }
}
