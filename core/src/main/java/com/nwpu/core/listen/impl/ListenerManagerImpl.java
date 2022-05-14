package com.nwpu.core.listen.impl;

import com.nwpu.core.listen.Listener;
import com.nwpu.core.listen.ListenerManager;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Junho
 * @date 2022/5/13 22:26
 */
@Slf4j
public class ListenerManagerImpl implements ListenerManager {

    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * 存放监听器，包括emm 命令监听等
     */
    private Set<Listener> listeners = new HashSet<>();

    /**
     * 接收到请求，第二步在这里，让每一个监听者都 accept 事件 ==》AbstractListener的accept
     * AbstractListener的accept ==》 具体监听者（如CommandListener）的accept0
     * @param event
     */
    private void accept0(final Object event) {
        if (!listeners.isEmpty()) {
            for (Listener listener : listeners) {
                listener.accept(event);
            }
        }
    }

    /**
     * 接收请求
     * 接收到请求，第一步在这里监听到 ==》 本实现类中的accept0
     * @param event
     */
    @Override
    public void accept(Object event) {
        executor.execute(() -> {
            accept0(event);
        });
    }

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void remove(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void removeAll() {
        listeners.clear();
    }
}
