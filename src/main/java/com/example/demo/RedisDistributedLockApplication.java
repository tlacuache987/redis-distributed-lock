package com.example.demo;

import java.util.Collections;

import org.redisson.Redisson;
import org.redisson.RedissonNode;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonNodeInitializer;
import org.redisson.config.Config;
import org.redisson.config.RedissonNodeConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedisDistributedLockApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisDistributedLockApplication.class, args);
	}

	@Bean
	public RMap<String, Object> redissonMap() {
		RMap<String, Object> map = redissonClient().getMap("redissonMap");
		return map;
	}

	@Bean
	public Config redissonConfig() {
		Config config = new Config();
		config.useSingleServer().setAddress(String.format("localhost:%s", 6379));
		return config;
	}

	@Bean
	public RedissonClient redissonClient() {
		RedissonClient client = Redisson.create(redissonConfig());
		return client;
	}

	@Bean(destroyMethod = "shutdown")
	public RedissonNode redissonNode() {
		RedissonNodeConfig nodeConfig = new RedissonNodeConfig(redissonConfig());
		nodeConfig.setExecutorServiceWorkers(Collections.singletonMap("myExecutor", 5));
		RedissonNode node = RedissonNode.create(nodeConfig, (Redisson) redissonClient());
		nodeConfig.setRedissonNodeInitializer(new MyRedissonNodeInitializer());
		System.out.println("node start");
		node.start();
		return node;
	}

	static class MyRedissonNodeInitializer implements RedissonNodeInitializer {

		@Override
		public void onStartup(RedissonNode redissonNode) {

			redissonNode.getRedisson().getExecutorService("myExecutor").registerWorkers(5);

		}

	}

}
