package cn.itcast.bos.dao.base;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Standard;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")//这是测试类的注解
public class StandardRepositoryTest {
@Autowired   //注入要被测试的类
	private StandardRepository standardRepository;
	

@Test
	public void testFindByName() {
		
		System.out.println(standardRepository.findByName("1-10"));
		
	}
	//测试方法一结束
	@Test
	public void test2FindByName() {
		List<cn.itcast.bos.domain.base.Standard> list = standardRepository.findByNameLike("%1%");
		
		for (Standard standard : list) {
			System.out.println("结果"+standard);
			
		}
	}
	//测试方法一结束
	@Test	
	public void test3FindByName() {
			//standardRepository.queryname1("1-10")
			System.out.println(standardRepository.queryname1("1-10"));
			
		}
	//测试方法一结束
	@Test	
	public void test4FindByName() {
			//standardRepository.queryname1("1-10")
			System.out.println(standardRepository.queryname2("1-10公斤"));
			
		}
	//d带参数的，修改的操作
	@Test
	@Transactional   //执行删改操作需要事务
	@Rollback(false)
	public void updateMinSize(){
		 
		standardRepository.updateMinSize(8,1);
		
	}
	
	
	
	
	
	
	
	
	
	
}
