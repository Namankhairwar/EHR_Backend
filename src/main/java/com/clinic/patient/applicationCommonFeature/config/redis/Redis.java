//package com.clinic.patient.applicationCommonFeature.config.redis;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//@Configuration
//public class Redis {
//    @Bean
//    public RedisTemplate set(RedisConnectionFactory redisConnectionFactory){
//      RedisTemplate o = new RedisTemplate();
//      o.setConnectionFactory(redisConnectionFactory);
//      o.setHashKeySerializer(new StringRedisSerializer());
//      o.setHashValueSerializer(new StringRedisSerializer());
//      return o;
//    }
//}
