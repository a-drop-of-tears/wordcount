package com.lwh.mr.rpc;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author lwh
 * @date 2019/6/20 15:38
 */
public class RPCClient<T> {

    public static <T> T getRemoteProxyObject(final Class<?> serviceInterface, final InetSocketAddress addr){
        //1.将本地接口调用转换为JDK的动态代理，在动态代理中实现接口的远程调用
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket=null;
                        ObjectInputStream inputStream=null;
                        ObjectOutputStream outputStream=null;
                        try {
                            //2.创建Socket客户端，根据指定地址连接远程服务提供者
                            socket=new Socket();
                            socket.connect(addr);
                            //3.将远程服务调用所需的接口类，方法名，参数列表等编码后发送给服务端提供服务
                            outputStream=new ObjectOutputStream(socket.getOutputStream());
                            outputStream.writeUTF(serviceInterface.getName());
                            outputStream.writeUTF(method.getName());
                            outputStream.writeObject(method.getParameterTypes());
                            outputStream.writeObject(args);

                            //4.同步阻塞等待服务器返回应答，获取应答后返回
                            inputStream=new ObjectInputStream(socket.getInputStream());
                            return inputStream.readObject();
                        }finally {
                            if (socket!=null){
                                socket.close();
                            }
                            if(outputStream!=null){
                                outputStream.close();
                            }
                            if(inputStream!=null){
                                inputStream.close();
                            }
                        }
                    }
                });
    }

}
