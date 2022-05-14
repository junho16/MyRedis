package com.nwpu.core.command.impl;

import com.nwpu.core.client.RedisClient;
import com.nwpu.core.command.AbstractCommand;
import com.nwpu.core.command.RedisCommand;
import com.nwpu.core.exception.ExceptionThrower;

/**
 * @author Junho
 * @date 2022/5/14 13:23
 */
public class AuthCommand extends AbstractCommand implements RedisCommand {

    private String password;

    protected AuthCommand(RedisClient redisClient, String password) {
        super(redisClient);
        this.password = password;
    }

    @Override
    public void execute0() {
        boolean auth = redisClient.server().auth(password);
        if (auth) {
            redisClient.dataAccess().setAuth(true);
            redisClient.stream().responseString("OK");
        } else {
            ExceptionThrower.AUTH_ERROR.throwException();
        }
    }

    @Override
    public String decode() {
        return "auth " + password;
    }

    public static AuthCommand build(RedisClient redisClient, String password) {
        return new AuthCommand(redisClient, password);
    }

    @Override
    public String toString() {
        return "AuthCommand{" +
                password +
                '}';
    }
}
