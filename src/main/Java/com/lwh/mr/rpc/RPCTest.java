package com.lwh.mr.rpc;

import org.apache.tools.ant.taskdefs.Sleep;

import java.net.InetSocketAddress;

/**
 * @author lwh
 * @date 2019/6/20 15:52
 */
public class RPCTest {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Server serviceServer=new ServerImpl(8088);
                    serviceServer.register(HelloService.class,HelloServiceImpl.class);
                    serviceServer.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        HelloService service=RPCClient.getRemoteProxyObject(HelloService.class,new InetSocketAddress("localhost",8088));
        System.out.println(service.sayHi("test"));
    }
}
