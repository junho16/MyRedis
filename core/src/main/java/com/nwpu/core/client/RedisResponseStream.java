package com.nwpu.core.client;

/**
 * @author Junho
 * @date 2022/5/14 11:39
 */
public interface RedisResponseStream {

    void response(RedisResponse msg);

    void responseString(String str);

    void response(Object object);

    void error(String error);

}
