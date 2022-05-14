package com.nwpu.core.listen.impl;

import com.nwpu.core.listen.Listener;

/**
 * @author Junho
 * @date 2022/5/14 10:27
 */
public abstract class AbstractListener<T> implements Listener {

    @Override
    public void accept(Object event) {
        accept0((T) event);
    }

    public abstract void accept0(T event);
}