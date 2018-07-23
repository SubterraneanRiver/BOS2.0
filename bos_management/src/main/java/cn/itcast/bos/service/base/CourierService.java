package cn.itcast.bos.service.base;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import cn.itcast.bos.domain.base.Courier;

public interface CourierService {
	public void save(Courier courier);
	
	public Page<Courier> findpageData(Specification<Courier> specification,Pageable pageable);

	//实现快递员的作废功能
	public void delBatch(String[] idArray);

	public void addBatch(String[] idArray);
	
}
