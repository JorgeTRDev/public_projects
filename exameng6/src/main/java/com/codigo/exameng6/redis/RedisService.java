package com.codigo.exameng6.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * RedisService class: Clase de configuración para Redis.
 *
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redisTemplate;

    public void guardarEnRedis(String key, String value, int exp){
        redisTemplate.opsForValue().set(key,value);
        redisTemplate.expire(key,exp, TimeUnit.MINUTES);
    }

    public String getDataDesdeRedis(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void borrarData(String key){
        redisTemplate.delete(key);
    }
}
