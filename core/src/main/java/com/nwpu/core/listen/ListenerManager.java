package com.nwpu.core.listen;

/**
 * @author Junho
 * @date 2022/5/13 16:06
 */
public interface ListenerManager extends Listener {
    void addListener(Listener listener);

    void remove(Listener listener);

    void removeAll();
}
