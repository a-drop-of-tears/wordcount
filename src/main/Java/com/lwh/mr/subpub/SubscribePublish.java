package com.lwh.mr.subpub;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 订阅器类
 * @author lwh
 * @date 2019/6/24 19:26
 */
public class SubscribePublish<M> {
    /**
     * 订阅器名称
     */
    private String name;

    /**
     * 订阅器队列容量
     */
    final int QUEUE_CAPACITY=20;

    /**
     * 订阅器存储队列
     */
    private BlockingQueue<Msg> queue=new ArrayBlockingQueue<Msg>(QUEUE_CAPACITY);

    /**
     * 订阅者
     */
    private List<ISubcriber> subcribers=new ArrayList<>();

    /**
     * 构造器
     * @param name 订阅者名称
     */
    public SubscribePublish(String name) {
        this.name = name;
    }

    public void publish(String publisher,M message,boolean isInstantMsg){
        if(isInstantMsg){
            update(publisher,message);
            return;
        }
        Msg<M> m=new Msg<M>(publisher,message);
        if(!queue.offer(m)){
            update();
        }
    }

    /**
     * 订阅
     * @param subcriber
     */
    public void subcribe(ISubcriber subcriber) {
        subcribers.add(subcriber);
    }

    /**
     * 退订
     * @param subcriber
     */
    public void unSubcribe(ISubcriber subcriber) {
        subcribers.remove(subcriber);
    }

    /**
     * 发送存储队列所有消息
     */
    public void update(){
        Msg m = null;
        while((m = queue.peek())!= null){
            this.update(m.getPublisher(),(M)m.getM());
        }
    }

    /**
     * 发送消息
     * @param publisher
     * @param Msg
     */
    public void update(String publisher,M Msg) {
        for(ISubcriber subcriber:subcribers){
            subcriber.update(publisher,Msg);
        }
    }

    class Msg<M>{
        private String publisher;
        private M m;

        public Msg(String publisher, M m) {
            this.publisher = publisher;
            this.m = m;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public M getM() {
            return m;
        }

        public void setM(M m) {
            this.m = m;
        }
    }
}
