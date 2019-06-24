package com.lwh.mr.subpub;

/**
 * 发布者接口
 * @author lwh
 * @date 2019/6/24 19:19
 */
public interface IPublisher<M> {

    /**
     * 向订阅者发布消息
     * @param subscribePublish 订阅器
     * @param message 消息
     * @param isInstantMsg 是否立即发送
     */
    void publish(SubscribePublish subscribePublish,M message,boolean isInstantMsg);
}
