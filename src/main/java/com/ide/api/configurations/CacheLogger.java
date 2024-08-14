package com.ide.api.configurations;

import com.ide.api.dto.CategorieDTO;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Slf4j
public class CacheLogger implements CacheEventListener<String, List<CategorieDTO>> {

    @Override
    public void onEvent(CacheEvent<? extends String, ? extends List<CategorieDTO>> cacheEvent) {
        log.info("Event '{}' fired for key '{}' with value {}", cacheEvent.getType(), cacheEvent.getKey(), cacheEvent.getNewValue());
    }

}
