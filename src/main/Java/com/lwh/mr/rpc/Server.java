package com.lwh.mr.rpc;

import java.io.IOException;

/**
 * @author lwh
 * @date 2019/6/20 15:12
 */
public interface Server {
    void stop();
    void start() throws IOException;
    void register(Class serviceInterface,Class impl);
    boolean isRunning();
    int getPort();
}
