package com.ide.api.configurations;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.cache.Caching;
//import javax.cache.CacheManager;
import javax.cache.spi.CachingProvider;


@Configuration
@EnableCaching
public class CacheConfig {

//    @Bean
//    public JCacheCacheManager cacheManager() {
//        return new JCacheCacheManager(cacheManagerFactory().getCacheManager());
//    }
//
//    @Bean
//    public javax.cache.CacheManager cacheManagerFactory() {
//        return Caching.getCachingProvider().getCacheManager();
//    }

}
