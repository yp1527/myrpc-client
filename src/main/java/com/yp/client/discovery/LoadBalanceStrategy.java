package com.yp.client.discovery;

import java.util.List;

/**
 * 负载均衡策略
 */
public interface LoadBalanceStrategy {

    String selectHost(List<String> repos);

}
