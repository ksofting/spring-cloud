package com.framework.common.cache.redis;

import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisConfig{
    private static Logger logger= Logger.getLogger(RedisConfig.class);
    
    @Bean
    @ConfigurationProperties(prefix= "spring.redis")
    public JedisPoolConfig getRedisConfig(){
        JedisPoolConfig config= new JedisPoolConfig();
        return config;
    }
    
    @Bean
    @ConfigurationProperties(prefix= "spring.redis")
    public JedisConfig getJedisConfig(){
        JedisConfig jedisConfig= new JedisConfig();
        return jedisConfig;
    }
    
    @Bean
    @ConfigurationProperties(prefix= "spring.redis")
    public JedisConnectionFactory getConnectionFactory(){
        JedisConnectionFactory factory= new JedisConnectionFactory();
        JedisPoolConfig config= getRedisConfig();
        factory.setPoolConfig(config);
        logger.info("JedisConnectionFactory bean init success.");
        return factory;
    }
    
    @Bean
    public RedisTemplate<?,?> getRedisTemplate(){
        RedisTemplate<?,?> template= new StringRedisTemplate(getConnectionFactory());
        logger.info("RedisTemplate bean init success.");
        return template;
    }
    
    @Bean
    public JedisPool getJedisPool(){
        JedisConfig config= getJedisConfig();
        JedisPool jedisPool=
            new JedisPool(getRedisConfig(),config.getHost(),config.getPort(),config.getTimeout(),config.getPassword(),config.getDatabase());
        logger.info("JedisPool bean init success.");
        return jedisPool;
    }
}
