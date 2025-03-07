package com.ide.api.controller;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("cache")
public class CacheController {

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/public/illustrations")
    public Map<Object, Object> getProductCache() {
        Cache<Object, Object> cache = (Cache<Object, Object>) cacheManager.getCache("illustrations").getNativeCache();
        return cache.asMap();
    }

    @GetMapping("/public/categories")
    public Map<Object, Object> getCategoryCache() {
        Cache<Object, Object> cache = (Cache<Object, Object>) cacheManager.getCache("categories").getNativeCache();
        return cache.asMap();
    }
}
