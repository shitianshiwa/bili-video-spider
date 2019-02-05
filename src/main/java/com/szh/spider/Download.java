package com.szh.spider;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;
import com.szh.spider.uitls.ElasticSearchPoolUtil;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.LoggerFactory;

public class Download implements Runnable{
    private String u = "https://api.bilibili.com/x/web-interface/view?aid=";
    private int i;
    private static final Logger logger = LoggerFactory.getLogger(Download.class);
    Download(int i){
        this.i = i;
    }

    @Override
    public void run() {

        logger.info("new thread! crawling av"+i);
        String url = u + String.valueOf(i);

        // 准备请求
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        request.setHeader("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8");
        request.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");

        request.setHeader("Sec-Fetch-Mode","navigate");
        request.setHeader("Cache-Control","max-age=0");
        request.setHeader("Host","api.bilibili.com");



        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try{
            response = httpClient.execute(request);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JSONObject json = JSONObject.parseObject(EntityUtils.toString(response.getEntity(), "utf-8"));
                logger.info(json.toJSONString());

                if(json.getIntValue("code") != 0){
                    logger.error("视频不存在: av"+ i );
                }
                else {
                    RestHighLevelClient client = ElasticSearchPoolUtil.getClient();
                    IndexRequest indexRequest = new IndexRequest("bili", "video",String.valueOf(i) ).source(json);
                    IndexResponse indexResponse = client.index(indexRequest);
                    ElasticSearchPoolUtil.returnClient(client);
                }
            } else {
                logger.error("返回状态不是200, 是"+ response.getStatusLine().getStatusCode());
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpClient!=null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
