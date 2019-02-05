package com.szh.spider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
