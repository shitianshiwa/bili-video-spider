package com.szh.spider.uitls;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.xml.ws.spi.http.HttpContext;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;


public class HttpConnectionPool {

    private static CloseableHttpClient httpClient;
    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private static PoolingHttpClientConnectionManager cm;
    static{
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(100);
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)     //是否立即发送数据，设置为true会关闭Socket缓冲，默认为false
                .setSoReuseAddress(true) //是否可以在一个进程关闭Socket后，即使它还没有释放端口，其它进程还可以立即重用端口
                .setSoTimeout(5000)       //接收数据的等待超时时间，单位ms
                //注意，此处会导致问题！！！！！！！！！！！！后文分析，会导致长时间持有连接池的锁
                .setSoLinger(60)         //关闭Socket时，要么发送完所有数据，要么等待60s后，就关闭连接，此时socket.close()是阻塞的
                .setSoKeepAlive(true)    //开启监视TCP连接是否有效
                .build();
        cm.setDefaultSocketConfig(socketConfig);
        ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                // Honor 'keep-alive' header
                HeaderElementIterator it = new BasicHeaderElementIterator(
                        response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch(NumberFormatException ignore) {
                        }
                    }
                }
                return 10 * 1000;
            }
        };
        httpClient = HttpClients.custom().setConnectionManager(cm)
                .setKeepAliveStrategy(myStrategy)
                .evictExpiredConnections()
                .evictIdleConnections(30,TimeUnit.SECONDS)
                .setConnectionReuseStrategy(DefaultClientConnectionReuseStrategy.INSTANCE)
                .build();
        //定时打印连接池状态
        executorService.scheduleAtFixedRate(() -> {
            System.out.println(cm.getTotalStats());
        },100L,1000L, TimeUnit.MILLISECONDS);
    }

    public static CloseableHttpClient getHttpClient(){
        return httpClient;
    }


}
