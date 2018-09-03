package com.example.demo._ejemplopedorro;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisLock implements Lock {

	protected StringRedisTemplate redisStringTemplate;

	private static final String LOCKED = "LOCKED";

	private static final long TIME_OUT = 10;

	public static final int EXPIRE = 60;

	private String key;

	private volatile boolean isLocked = false;

	public RedisLock(String key, StringRedisTemplate stringRedisTemplate) {
		this.key = key;
		this.redisStringTemplate = stringRedisTemplate;
	}

	@Override
	public synchronized void lock() {
		
		long nowTime = System.currentTimeMillis();
		long timeout = TIME_OUT * 1000;
		final Random r = new Random();
		try {
			while //((System.currentTimeMillis() - nowTime) < timeout) {
				/*if*/ (redisStringTemplate.getConnectionFactory().getConnection().setNX(key.getBytes(),
						LOCKED.getBytes())) {
					
					//System.out.println("setting "+key+" with expire of "+EXPIRE+" seconds");
					redisStringTemplate.expire(key, EXPIRE, TimeUnit.SECONDS);
					isLocked = true;
					//break;
				//}

				Thread.sleep(3, r.nextInt(500));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void unlock() {
		//System.out.println("is locked? " + isLocked);
		if (isLocked) {
			//System.out.println("deleting key");
			redisStringTemplate.delete(key);
		}
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}
}