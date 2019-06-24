package com.lwh.mr.subpub;

/**
 * @author lwh
 * @date 2019/6/24 19:42
 */
public class SubcribeImpOne<M> implements ISubcriber<M> {

    private String name;

    public SubcribeImpOne(String name) {
        this.name = name;
    }

    @Override
    public void subcribe(SubscribePublish subscribePublish) {
        subscribePublish.subcribe(this);
    }

    @Override
    public void unSubcribe(SubscribePublish subscribePublish) {
        subscribePublish.unSubcribe(this);
    }

    @Override
    public void update(String publisher, M message) {
        System.out.println(this.name+"收到"+publisher+"发来的消息"+message.toString());
    }
}
