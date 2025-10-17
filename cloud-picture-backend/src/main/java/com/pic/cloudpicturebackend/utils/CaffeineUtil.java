package com.pic.cloudpicturebackend.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * Caffeine 缓存工具类
 */
public class CaffeineUtil {

    private static final Cache<String, String> LOCAL_CACHE = Caffeine.newBuilder()
            .initialCapacity(1024)
            .maximumSize(10_000L)   // 最大 10000 条数据
            // 缓存 5 分钟后移除
            .expireAfterWrite(5, TimeUnit.MINUTES)
            .build();

    public static Cache<String, String> getCache() {
        return LOCAL_CACHE;
    }
}
