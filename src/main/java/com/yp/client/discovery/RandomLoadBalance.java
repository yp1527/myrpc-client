package com.yp.client.discovery;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡
 */
public class RandomLoadBalance extends AbstractLoadBalance{

    @Override
    protected String doSelect(List<String> repos) {
        int length=repos.size();
        //从repos的集合内容随机获得一个地址
        Random random=new Random();
        return repos.get(random.nextInt(length));
    }
}
