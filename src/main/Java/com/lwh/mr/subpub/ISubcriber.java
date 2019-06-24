package com.lwh.mr.subpub;

/**
 * 订阅者接口
 * @author lwh
 * @date 2019/6/24 19:21
 */
public interface ISubcriber<M> {
    /**
     * 订阅
     * @param subscribePublish 订阅器
     */
    void subcribe(SubscribePublish subscribePublish);


    /**
     * 退订
     * @param subscribePublish 订阅器
     */
    void unSubcribe(SubscribePublish subscribePublish);

    /**
     * 接收消息
     * @param publisher 发布者
     * @param message 消息
     */
    void update(String publisher,M message);
}
