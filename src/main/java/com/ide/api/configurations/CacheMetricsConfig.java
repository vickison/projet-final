package com.ide.api.configurations;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CacheMetricsConfig {

//    @Bean
//    public CacheMetricsRegistrar cacheMetricsRegistrar(CacheManager cacheManager, MeterRegistry registry) {
//        return new CacheMetricsRegistrar(cacheManager, registry);
//    }
}
