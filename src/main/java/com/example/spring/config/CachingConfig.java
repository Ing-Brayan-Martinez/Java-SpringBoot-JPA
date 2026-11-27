package com.example.spring.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de caché de la aplicación.
 */
@Configuration
@EnableCaching
@EnableAutoConfiguration
public class CachingConfig {

    /**
     * Método para crear la instancia del almacén de la caché.
     * @return Retorna el almacén de caché.
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("orderCache", "taskCache",
                "productCache");
    }
}
