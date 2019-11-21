package com.yp.client.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * zk服务发现地址
 */
public class ServiceDiscoveryWithZk implements IServiceDiscovery{

    CuratorFramework curatorFramework =null;
    //服务地址的本地缓存
    List<String> serviceRepos=new ArrayList<>();

    {
        //初始化zookeeper的连接， 会话超时时间是5s，衰减重试
        curatorFramework = CuratorFrameworkFactory.builder().
                connectString(ZkConfig.CONNECTION_STR).sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000, 3)).
                namespace("registry")//命名空间
                .build();
        curatorFramework.start();
    }

    /**
     * 服务发现
     * @param serviceName
     * @return
     */
    @Override
    public String discovery(String serviceName) {
        String path="/"+serviceName; //registry/com.gupaoedu.demo.HelloService
        System.out.println("节点路径:"+path);
        if(serviceRepos.isEmpty()) {
            try {
                serviceRepos = curatorFramework.getChildren().forPath(path);
                //设置节点的监听事件
                registryWatch(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //针对已有的地址做负载均衡
        LoadBalanceStrategy loadBalanceStrategy=new RandomLoadBalance();
        return loadBalanceStrategy.selectHost(serviceRepos);
    }

    /**
     * 节点监听事件（当节点发生变化的时候 更新地址列表）
     * @param path
     * @throws Exception
     */
    private void registryWatch(final String path) throws Exception {
        PathChildrenCache nodeCache=new PathChildrenCache(curatorFramework,path,true);
        PathChildrenCacheListener nodeCacheListener= (curatorFramework1, pathChildrenCacheEvent) -> {
            System.out.println("客户端收到节点变更的事件");
            // 再次更新本地的缓存地址
            serviceRepos=curatorFramework1.getChildren().forPath(path);
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();

    }
}
