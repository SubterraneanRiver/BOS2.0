package bos_fore;


import org.junit.Test;

import redis.clients.jedis.Jedis;

public class JedisTest {

	@Test
	public void jedistest1() throws Exception {
		//连接jedis的端口
		Jedis jedis = new Jedis("localhost",6379); 
		jedis.select(2);
		//带有效期的
		jedis.setex("company",20, "大大公司");
		
		System.out.println(jedis.get("company"));
		
	}
	
}
