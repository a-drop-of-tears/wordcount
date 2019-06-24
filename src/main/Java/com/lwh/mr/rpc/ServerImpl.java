package com.lwh.mr.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author lwh
 * @date 2019/6/20 15:14
 */
public class ServerImpl implements Server {

    private static ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final HashMap<String, Class> serviceRegistry = new HashMap<String, Class>();

    private static boolean isRunning = false;

    private static int port;

    public ServerImpl(int port) {
        ServerImpl.port=port;
    }

    @Override
    public void stop() {
        isRunning=false;
        executorService.shutdown();
    }

    @Override
    public void start() throws IOException {
        ServerSocket serverSocket=new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("start server");
        try {
            while (true){
                //监听客户端的tcp连接，接收TCp连接后将其封装在task，由线程池去执行
                executorService.execute(new ServerTask(serverSocket.accept()));
            }
        }finally {
            serverSocket.close();
        }
    }

    @Override
    public void register(Class serviceInterface, Class impl) {
        serviceRegistry.put(serviceInterface.getName(),impl);
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPort() {
        return port;
    }

    private static class ServerTask implements Runnable{
        Socket client=null;

        public ServerTask(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            ObjectInputStream inputStream=null;
            ObjectOutputStream outputStream=null;
            try{
                //2.将客户端发送的码流反序列化成对象，反射调用服务实现者，获取执行结果
                inputStream = new ObjectInputStream(client.getInputStream());
                String serviceName=inputStream.readUTF();
                String methodName=inputStream.readUTF();
                Class<?>[] parameterTypes = (Class<?>[]) inputStream.readObject();
                Object[] arguments = (Object[]) inputStream.readObject();
                Class serverClass = serviceRegistry.get(serviceName);
                if(serverClass==null){
                    throw new ClassNotFoundException(serviceName+"not found");
                }
                Method method = serverClass.getMethod(methodName, parameterTypes);
                Object result=method.invoke(serverClass.newInstance(),arguments);
                //3.将执行结果反序列化，通过socket发送给客户端
                outputStream=new ObjectOutputStream(client.getOutputStream());
                outputStream.writeObject(result);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(outputStream!=null){
                    try {
                        outputStream.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if (inputStream!=null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (client!=null){
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
