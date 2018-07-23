package cn.itcast.crm.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public List<Customer> findNoAssociationCustomers() {
		return customerRepository.findByFixedAreaIdIsNull();
	}
	@Override
	public List<Customer> findHasAssciationFixedAreaCustomers(String fixedAreaId) {
		return customerRepository.findByFixedAreaId(fixedAreaId);
	}

	@Override
	public void assoicationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {
		//执行解除关联动作
		customerRepository.clearFixedAreaId(fixedAreaId);
		
		// 切割支字符串1，2，3
//首先判断传上来的数组是否为空的
		if(StringUtils.isBlank(customerIdStr)){
			return;  //如果为空就直接结束
		}
		
		String[] customerIdArray = customerIdStr.split(",");// 根据，进行切割
		for (String idStr : customerIdArray) {
			Integer id = Integer.parseInt(idStr);
			customerRepository.updateFixedAreaId(fixedAreaId, id);
		}
	}

}
