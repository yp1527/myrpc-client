package com.yp.client;


import com.yp.client.discovery.IServiceDiscovery;
import com.yp.myrpc.server.domain.RpcRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * 处理远程通信的handler
 */
public class RpcClientHandler implements InvocationHandler {

    private IServiceDiscovery serviceDiscovery;
    private String version;

    public RpcClientHandler(IServiceDiscovery serviceDiscovery, String version) {
        this.serviceDiscovery = serviceDiscovery;
        this.version = version;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object object=null;
        RpcRequest rpcRequest=new RpcRequest();
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setArgs(args);
        rpcRequest.setVersion(version);
        ObjectInputStream objectInputStream=null;
        ObjectOutputStream objectOutputStream=null;
        try {
            String serviceName=rpcRequest.getClassName();
            if(version!=null&&!"".equals(version)){
                serviceName=serviceName+"-"+version;
            }
            //从zookeeper获取地址(ip地址:端口号)
            String serviceAddress=serviceDiscovery.discovery(serviceName);
            String urls[]=serviceAddress.split(":");
            Socket socket=new Socket(urls[0],Integer.parseInt(urls[1]));
            objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(rpcRequest);
            //接收值
            objectInputStream =new ObjectInputStream(socket.getInputStream());
            object=objectInputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(objectInputStream!=null){
                    objectInputStream.close();
                }
                if(objectOutputStream!=null){
                    objectOutputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return object;
    }
}
