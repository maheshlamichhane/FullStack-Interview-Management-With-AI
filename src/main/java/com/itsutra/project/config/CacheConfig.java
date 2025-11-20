//package com.itsutra.project.config;
//
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.Duration;
//
//@Configuration
//@EnableCaching
//public class CacheConfig {
//
//
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(30))
//                .disableCachingNullValues()
//                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//
//        return RedisCacheManager.builder(factory)
//                .cacheDefaults(config)
//                .withInitialCacheConfigurations(getCacheConfigurations())
//                .build();
//    }
//
//    private Map<String, RedisCacheConfiguration> getCacheConfigurations() {
//        Map<String, RedisCacheConfiguration> configs = new HashMap<>();
//        configs.put("interviews", RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(10)));
//        configs.put("participants", RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(5)));
//        return configs;
//    }
//}
