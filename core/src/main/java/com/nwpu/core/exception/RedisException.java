package com.nwpu.core.exception;

/**
 * @author Junho
 * @date 2022/5/13 14:15
 */
public class RedisException extends RuntimeException {

    public RedisException(String errorMsg) {
        super(errorMsg);
    }

}
