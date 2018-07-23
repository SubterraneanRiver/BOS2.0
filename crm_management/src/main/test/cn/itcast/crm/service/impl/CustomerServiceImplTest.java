package cn.itcast.crm.service.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.crm.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CustomerServiceImplTest {
@Autowired
	private CustomerService customerService;
	
	@Test
	public void testFindNoAssociationCustomers() {
	
		System.out.println("查询未关联执行");
		System.out.println(customerService.findNoAssociationCustomers());
	
	}

	@Test
	public void testFindHasAssciationFixedAreaCustomers() {
		System.out.println(customerService.findHasAssciationFixedAreaCustomers("3121"));
		
	}

	@Test
	public void testAssoicationCustomersToFixedArea() {
		//测试关联
		
		customerService.assoicationCustomersToFixedArea("1,2", "3121");
		
		
		
	}

}
