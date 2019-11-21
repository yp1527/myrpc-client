package com.yp.client.discovery;

import java.util.List;

/**
 * 负载均衡抽象类
 */
public abstract class AbstractLoadBalance implements LoadBalanceStrategy{
    /**
     * 模板模式
     * @param repos
     * @return
     */
    @Override
    public String selectHost(List<String> repos) {
        //repos可能为空， 可能只有一个。
        if(repos==null||repos.size()==0){
            return null;
        }
        if(repos.size()==1){
            return repos.get(0);
        }
        return doSelect(repos);
    }

    protected abstract String doSelect(List<String> repos);

}
