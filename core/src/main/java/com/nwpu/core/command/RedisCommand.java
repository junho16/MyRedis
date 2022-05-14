package com.nwpu.core.command;

public interface RedisCommand {

    void execute();

    String decode();

}