package com.szh.spider;

import com.alibaba.fastjson.JSONObject;
import com.szh.spider.uitls.ElasticSearchPoolUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;




/**
 * @ClassName GetJson
 * @Description TODO
 * @Author szh
 * @Date 2020/4/4 11:32
 * @Version 1.0
 **/




public class Main {



    public static void main(String[] args) throws Exception {
        ExecutorService excutor = Executors.newFixedThreadPool(10);
        int i = 97921280;
        String u = "https://api.bilibili.com/x/web-interface/view?aid=";
        while(i>0){
            excutor.execute(new Download(i));
            Thread.sleep(500);
            i--;
        }
    }
}
