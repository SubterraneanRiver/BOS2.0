package com.lz.test;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class Spring_data_RedisTest {
//对象注入
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

@Test
public void t1() throws Exception {
	
	//设置值，和时间
	redisTemplate.opsForValue().set("city", "北京",50,TimeUnit.SECONDS);
	
	System.out.println(redisTemplate.opsForValue().get("city"));
	
}


}
