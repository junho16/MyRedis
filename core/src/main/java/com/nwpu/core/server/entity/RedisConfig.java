package com.nwpu.core.server.entity;

import lombok.Data;

/**
 * @author Junho
 * @date 2022/5/13 16:09
 */
@Data
public abstract class RedisConfig {

    private static final String DEFAULT_HOST = "localhost";

    private static final int DEFAULT_PORT = 6379;

    private static final int DEFAULT_DB_NUM = 16;

    private static final String DEFAULT_PASSWORD = "root";

    protected String host = DEFAULT_HOST;

    protected int port = DEFAULT_PORT;

    protected int dbNum = DEFAULT_DB_NUM;

    protected String password = DEFAULT_PASSWORD;

}