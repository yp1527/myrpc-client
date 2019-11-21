package com.yp.client;


import com.yp.myrpc.server.api.IHelloServer;
import com.yp.myrpc.server.api.IUserServer;

/**
 * 测试
 */
public class App {
    public static void main( String[] args )throws Exception{
        RpcProxyClient client=new RpcProxyClient();
        IHelloServer helloServer=client.getProxy("v2.0",IHelloServer.class);
        System.out.println(helloServer.sayHello("YangPing"));;

        IUserServer userServer=client.getProxy("",IUserServer.class);
        userServer.getAllUser();

        for(int i=0;i<100;i++) {
            Thread.sleep(2000);
            System.out.println(helloServer.sayHello("yangping"+i));
            userServer.getAllUser();
        }
    }
}
