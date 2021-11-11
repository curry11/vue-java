package com.markerhub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	//Redis序列化方式，以什么样的数据格式存入内存中
	@Bean
	RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {  //redisConnectionFactory redis连接池

		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);

		//redis序列化方式
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
		jackson2JsonRedisSerializer.setObjectMapper(new ObjectMapper());

		redisTemplate.setKeySerializer(new StringRedisSerializer());//key是String
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer); //vaule 是json

		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

		return redisTemplate;

	}

}
