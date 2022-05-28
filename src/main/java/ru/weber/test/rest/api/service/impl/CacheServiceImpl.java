package ru.weber.test.rest.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.weber.test.rest.api.service.CacheService;

import java.util.Objects;

@Service
public class CacheServiceImpl implements CacheService {

    private final CacheManager cacheManager;

    @Autowired
    public CacheServiceImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void evictAll() {
        cacheManager.getCacheNames()
                .forEach(name -> Objects.requireNonNull(cacheManager.getCache(name)).clear());
    }

    @Scheduled(fixedRate = 120000)
    private void evictAllcashesAtIntervals() {
        evictAll();
    }
}
