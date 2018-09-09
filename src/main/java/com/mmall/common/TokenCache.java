package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @program: mmall
 * @author: RoninLee
 * @Email: zlli233@qq.com
 * @create: 2018-09-09 20:58
 * @description: 实现本地缓存
 **/
public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    //LRU算法 当缓存数量达到最大时，LoadingCache会通过LRU算法找到使用次数最少的进行清除
    private static LoadingCache<String,String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key,String value){
        localCache.put(key, value);
    }

    public static String getKey(String key){
        String value = null;
        try {
            value  = localCache.get(key);
            if ("null".equals(value)){
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.error("localCache get error",e);
        }
        return null ;
    }

}
