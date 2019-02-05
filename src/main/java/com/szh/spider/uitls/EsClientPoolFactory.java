package com.szh.spider.uitls;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * EliasticSearch连接池工厂对象
 */
public class EsClientPoolFactory implements PooledObjectFactory<RestHighLevelClient>{
    @Override
    public void activateObject(PooledObject<RestHighLevelClient> arg0) throws Exception {

    }

    /**
     * 销毁对象
     */
    @Override
    public void destroyObject(PooledObject<RestHighLevelClient> pooledObject) throws Exception {
        RestHighLevelClient highLevelClient = pooledObject.getObject();
        highLevelClient.close();
    }

    /**
     * 生产对象
     */
//  @SuppressWarnings({ "resource" })
    @Override
    public PooledObject<RestHighLevelClient> makeObject() throws Exception {
//      Settings settings = Settings.builder().put("cluster.name","elasticsearch").build();
        RestHighLevelClient client = null;
        try {
            /*client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"),9300));*/
            client = new RestHighLevelClient( RestClient.builder( new HttpHost("127.0.0.1", 9200, "http")));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DefaultPooledObject<RestHighLevelClient>(client);
    }

    @Override
    public void passivateObject(PooledObject<RestHighLevelClient> arg0) throws Exception {
    }

    @Override
    public boolean validateObject(PooledObject<RestHighLevelClient> arg0) {
        return true;
    }
}