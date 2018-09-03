package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
//@EnableRedisHttpSession
public class SessionConfig /* extends AbstractHttpSessionApplicationInitializer */ {
	@Bean
	public JedisConnectionFactory connectionFactory() {
		return new JedisConnectionFactory();
	}
}