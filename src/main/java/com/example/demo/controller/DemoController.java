package com.example.demo.controller;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.redisson.api.RExecutorService;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.annotation.RInject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

@RestController
@SessionAttributes({ "userId" })
public class DemoController {

	@Autowired
	private RedissonClient client;

	@Autowired
	private RMap<String, Object> redissonMap;

	@Value("${server.port}")
	private String port;

	@RequestMapping(path = "/putValue")
	public String putValue(@RequestParam(name = "key") String key, @RequestParam(name = "value") String value,
			HttpSession session, Model model) {
		System.out.println("zaz");
		redissonMap.put(key, value);

		model.addAttribute("userId", key);
		System.out.println("userId: " + key);
		System.out.println("session: " + session.getId());
		return "ok from: " + port;
	}

	@RequestMapping(path = "/getValue")
	public Object getValue(@RequestParam(name = "key") String key, @ModelAttribute("userId") String userId,
			HttpSession session) {

		System.out.println("userId: " + userId);
		System.out.println("session: " + session.getId());
		return redissonMap.get(key) + ", from: " + port;
	}

	@RequestMapping(path = "/lock", method = RequestMethod.GET)
	public String lock(HttpSession session/*, @ModelAttribute("userId") String userId*/) {
		RLock myLock = client.getLock("myLock");

		try {
			System.out.println("aquiring LOCK");
			myLock.lock(15, TimeUnit.SECONDS);
			System.out.println("aquired LOCK successfully by: " + session.getId() /*+ ", userId: " + userId*/);

			Thread.sleep(10000);
			System.out.println("LOCK example executed successfully");

		} catch (Exception ex) {
			System.err.println("error has thrown " + ex.getMessage());
		} finally {
			myLock.unlock();
		}

		return "hello from: " + port;
	}

	@RequestMapping(path = "/executors", method = RequestMethod.GET)
	public String executors() {

		RExecutorService e = client.getExecutorService("myExecutor");
		e.execute(new RunnableTask());
		e.submit(new CallableTask());

		return "ok from: " + port;
	}

	public static class RunnableTask implements Runnable {

		@RInject
		RedissonClient redisson;

		@Override
		public void run() {
			RMap<String, String> map = redisson.getMap("myMap");
			map.put("5", "11");
			System.out.println("putts key: " + 5 + " with value: " + 11);
		}

	}

	public static class CallableTask implements Callable<String> {

		@RInject
		RedissonClient redisson;

		@Override
		public String call() throws Exception {
			RMap<String, String> map = redisson.getMap("myMap");
			map.put("1", "2");
			System.out.println("putts key: " + 1 + " with value: " + 2);
			System.out.println("returning key: " + 3);
			return map.get("3");
		}

	}
}
