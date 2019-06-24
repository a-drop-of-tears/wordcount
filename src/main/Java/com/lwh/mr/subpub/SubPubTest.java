package com.lwh.mr.subpub;

/**
 * @author lwh
 * @date 2019/6/24 19:45
 */
public class SubPubTest {
    public static void main(String[] args) {
        SubscribePublish<String> subscribePublish=new SubscribePublish<>("订阅器");
        IPublisher<String> publisher1=new PublisherImpOne<>("发布者1");
        ISubcriber<String> subcriber1=new SubcribeImpOne<>("订阅者1");
        ISubcriber<String> subcriber2=new SubcribeImpOne<>("订阅者2");

        subcriber1.subcribe(subscribePublish);
        subcriber2.subcribe(subscribePublish);

        publisher1.publish(subscribePublish,"welcome",true);
        publisher1.publish(subscribePublish,"to",true);
        publisher1.publish(subscribePublish,"yy",true);
    }
}
