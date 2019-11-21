package com.yp.client.discovery;

/**
 * 定义发现服务地址的接口
 */
public interface IServiceDiscovery {

    //根据服务名称返回服务地址
    String  discovery(String serviceName);
}
