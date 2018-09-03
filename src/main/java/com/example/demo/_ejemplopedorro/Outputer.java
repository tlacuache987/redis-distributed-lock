package com.example.demo._ejemplopedorro;

import lombok.SneakyThrows;

public class Outputer {

	RedisLock redisLock;
	
	public Outputer(RedisLock redisLock) {
		this.redisLock = redisLock;
	}

	@SneakyThrows
	public void output(String name) {
		//System.out.println("setting lock");
		redisLock.lock();
		try {
			for(int i=0; i<name.length(); i++) {
                System.out.print(name.charAt(i));
                Thread.sleep(100);
            }
			System.out.println();
		} finally {
			//System.out.println("release lock");
			redisLock.unlock();
		}
	}
}