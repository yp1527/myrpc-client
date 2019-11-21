package com.yp.client;

import com.yp.client.discovery.IServiceDiscovery;
import com.yp.client.discovery.ServiceDiscoveryWithZk;

import java.lang.reflect.Proxy;

public class RpcProxyClient {
    //zookeeper注册中心
    private IServiceDiscovery serviceDiscovery=new ServiceDiscoveryWithZk();
    public <T> T getProxy(String version,Class<T> interfaceCls){
        return (T)Proxy.newProxyInstance(interfaceCls.getClassLoader(),new Class[]{interfaceCls},new RpcClientHandler(serviceDiscovery,version));
    }
}
