# bili-video-spider

本来是19年写的一个玩具, 当时B站好像是不墙爬虫的, 不爬被墙 

经过测试, 在不用代理池的情况下, 单个IP爬虫间隔时间大概需要500ms

* 轻量级的用java编写的B站爬虫, 爬取视频所有信息, 采用Elasticsearch 6.5.3存储数据

* 主要采用线程池,HttpClient连接池和RestHighLevelClient连接池. 
* 主要用于通过Elasticsearch 聚合查询, 实现数据可视化操作.









