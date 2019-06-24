package com.lwh.mr.subpub;

/**
 * 发布者实现类
 * @author lwh
 * @date 2019/6/24 19:40
 */
public class PublisherImpOne<M> implements IPublisher<M> {

    private String name;

    public PublisherImpOne(String name) {
        this.name = name;
    }

    @Override
    public void publish(SubscribePublish subscribePublish, M message, boolean isInstantMsg) {
        subscribePublish.publish(this.name,message,isInstantMsg);
    }
}
