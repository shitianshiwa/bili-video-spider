# bili-video-spider

本来是19年写的一个玩具, 当时B站好像是不墙爬虫的, 不爬被墙 

经过测试, 在不用代理池的情况下, 单个IP爬虫间隔时间大概需要500ms

* 轻量级的用java编写的B站爬虫, 爬取视频所有信息, 采用Elasticsearch 6.5.3存储数据

* 主要采用线程池,HttpClient连接池和RestHighLevelClient连接池. 
* 主要用于通过Elasticsearch 聚合查询, 实现数据可视化操作.

## 部分数据展示

![image-20200405120348419](doc/image-20200405120348419.png)

## Elasticsearch 索引

```typescript
PUT /bili
{
    "settings":{
        "number_of_shards" : 3,
        "number_of_replicas" : 0
    },
    "mappings":{
        "video":{
            "properties":{
                "code": {
                    "type": "long"
                },
                "data": {
                    "properties": {
                        "aid": {
                            "type": "long"
                        },
                        "attribute": {
                            "type": "long"
                        },
                        "bvid": {
                            "type": "text",
                            "fields": {
                                "keyword": {
                                    "type": "keyword",
                                    "ignore_above": 256
                                }
                            }
                        },
                        "cid": {
                            "type": "long"
                        },
                        "copyright": {
                            "type": "long"
                        },
                        "ctime": {
                            "type": "date"
                        },
                        "desc": {
                            "type": "text",
                            "analyzer":"ik_max_word"
                        },
                        "dimension": {
                            "properties": {
                                "height": {
                                    "type": "long"
                                },
                                "rotate": {
                                    "type": "long"
                                },
                                "width": {
                                    "type": "long"
                                }
                            }
                        },
                        "duration": {
                            "type": "long"
                        },
                        "dynamic": {
                            "type": "text",
                            "analyzer":"ik_max_word"
                        },
                        "mission_id": {
                            "type": "long"
                        },
                        "no_cache": {
                            "type": "boolean"
                        },
                        "owner": {
                            "properties": {
                                "face": {
                                    "type": "text",
                                    "fields": {
                                        "keyword": {
                                            "type": "keyword",
                                            "ignore_above": 256
                                        }
                                    }
                                },
                                "mid": {
                                    "type": "long"
                                },
                                "name": {
                                    "type": "text",
                                    "analyzer":"ik_max_word"
                                }
                            }
                        },
                        "pages": {
                            "properties": {
                                "cid": {
                                    "type": "long"
                                },
                                "dimension": {
                                    "properties": {
                                        "height": {
                                            "type": "long"
                                        },
                                        "rotate": {
                                            "type": "long"
                                        },
                                        "width": {
                                            "type": "long"
                                        }
                                    }
                                },
                                "duration": {
                                    "type": "long"
                                },
                                "from": {
                                    "type": "text",
                                    "fields": {
                                        "keyword": {
                                            "type": "keyword",
                                            "ignore_above": 256
                                        }
                                    }
                                },
                                "page": {
                                    "type": "long"
                                },
                                "part": {
                                    "type": "text",
                                    "fields": {
                                        "keyword": {
                                            "type": "keyword",
                                            "ignore_above": 256
                                        }
                                    }
                                },
                                "vid": {
                                    "type": "text",
                                    "fields": {
                                        "keyword": {
                                            "type": "keyword",
                                            "ignore_above": 256
                                        }
                                    }
                                },
                                "weblink": {
                                    "type": "text",
                                    "fields": {
                                        "keyword": {
                                            "type": "keyword",
                                            "ignore_above": 256
                                        }
                                    }
                                }
                            }
                        },
                        "pic": {
                            "type": "text",
                            "fields": {
                                "keyword": {
                                    "type": "keyword",
                                    "ignore_above": 256
                                }
                            }
                        },
                        "pubdate": {
                            "type": "date"
                        },
                        "rights": {
                            "properties": {
                                "autoplay": {
                                    "type": "long"
                                },
                                "bp": {
                                    "type": "long"
                                },
                                "download": {
                                    "type": "long"
                                },
                                "elec": {
                                    "type": "long"
                                },
                                "hd5": {
                                    "type": "long"
                                },
                                "is_cooperation": {
                                    "type": "long"
                                },
                                "movie": {
                                    "type": "long"
                                },
                                "no_background": {
                                    "type": "long"
                                },
                                "no_reprint": {
                                    "type": "long"
                                },
                                "pay": {
                                    "type": "long"
                                },
                                "ugc_pay": {
                                    "type": "long"
                                },
                                "ugc_pay_preview": {
                                    "type": "long"
                                }
                            }
                        },
                        "stat": {
                            "properties": {
                                "aid": {
                                    "type": "long"
                                },
                                "coin": {
                                    "type": "long"
                                },
                                "danmaku": {
                                    "type": "long"
                                },
                                "dislike": {
                                    "type": "long"
                                },
                                "evaluation": {
                                    "type": "text",
                                    "fields": {
                                        "keyword": {
                                            "type": "keyword",
                                            "ignore_above": 256
                                        }
                                    }
                                },
                                "favorite": {
                                    "type": "long"
                                },
                                "his_rank": {
                                    "type": "long"
                                },
                                "like": {
                                    "type": "long"
                                },
                                "now_rank": {
                                    "type": "long"
                                },
                                "reply": {
                                    "type": "long"
                                },
                                "share": {
                                    "type": "long"
                                },
                                "view": {
                                    "type": "long"
                                }
                            }
                        },
                        "state": {
                            "type": "long"
                        },
                        "subtitle": {
                            "properties": {
                                "allow_submit": {
                                    "type": "boolean"
                                }
                            }
                        },
                        "tid": {
                            "type": "long"
                        },
                        "title": {
                            "type": "text",
                            "analyzer":"ik_max_word"
                        },
                        "tname": {
                            "type": "text",
                            "fields": {
                                "keyword": {
                                    "type": "keyword",
                                    "ignore_above": 256
                                }
                            }
                        },
                        "videos": {
                            "type": "long"
                        }
                    }
                },
                "message": {
                    "type": "text",
                    "fields": {
                        "keyword": {
                            "type": "keyword",
                            "ignore_above": 256
                        }
                    }
                },
                "ttl": {
                    "type": "long"
                }
            }
        }
    }
}
```






