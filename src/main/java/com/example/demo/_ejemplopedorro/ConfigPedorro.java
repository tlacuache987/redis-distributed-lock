package com.example.demo._ejemplopedorro;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import redis.clients.jedis.JedisPoolConfig;

//@Configuration
public class ConfigPedorro {
	@Bean
	public RedisConnectionFactory connectionFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(5);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);

		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
		connectionFactory.setUsePool(true);
		connectionFactory.setHostName("localhost");
		connectionFactory.setPort(6379);

		return connectionFactory;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		return new StringRedisTemplate(connectionFactory());
	}

	@Bean
	public RedisLock redisLock() {
		return new RedisLock("mylock", stringRedisTemplate());
	}

	@Bean
	public Outputer outputer() {
		return new Outputer(redisLock());
	}
}
