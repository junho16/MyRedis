package com.nwpu.core.listen.impl;

import com.nwpu.core.command.RedisCommand;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Junho
 * @date 2022/5/14 10:28
 */
@Slf4j
public class CommandListener extends AbstractListener<RedisCommand> {

    @Override
    public void accept0(RedisCommand event) {
        log.info("接收到了事件:{}", event.decode());
        log.info("accept command:{}", event.decode());
    }

}

