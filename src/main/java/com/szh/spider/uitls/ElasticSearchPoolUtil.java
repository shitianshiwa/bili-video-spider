package com.szh.spider.uitls;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * ElasticSearch 连接池工具类
 *
 */
public class ElasticSearchPoolUtil {

    private static GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    // 采用默认配置maxTotal是8，池中有8个client
    {
        poolConfig.setMaxTotal(8);
    }

    private static EsClientPoolFactory esClientPoolFactory = new EsClientPoolFactory();

    private static GenericObjectPool<RestHighLevelClient> clientPool = new GenericObjectPool<>(esClientPoolFactory,
            poolConfig);

    /**
     * 获得对象
     *
     * @return
     * @throws Exception
     */
    public static RestHighLevelClient getClient() throws Exception {
        RestHighLevelClient client = clientPool.borrowObject();
        return client;
    }

    /**
     * 归还对象
     *
     * @param client
     */
    public static void returnClient(RestHighLevelClient client) {
        clientPool.returnObject(client);
    }
}