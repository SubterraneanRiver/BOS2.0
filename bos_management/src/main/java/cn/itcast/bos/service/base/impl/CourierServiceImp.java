package cn.itcast.bos.service.base.impl;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImp implements CourierService {

	@Autowired
	private CourierRepository courierRepository;

	@Override
	
	@RequiresPermissions("courier：add")
	public void save(Courier courier) {

		courierRepository.save(courier);

	}

	@Override
	public Page<Courier> findpageData(Specification<Courier> specification, Pageable pageable) {
		// TODO Auto-generated method stub
		return courierRepository.findAll(specification, pageable);
	}

	// 将快递员fe废除
	@Override
	public void delBatch(String[] idArray) {

		for (String idstr : idArray) {
			Integer id = Integer.parseInt(idstr);
			courierRepository.updateDelTag(id);

		}

	}
	// 将快递员状态还原
	@Override
	public void addBatch(String[] idArray) {
		for (String idstr : idArray) {
			Integer id = Integer.parseInt(idstr);
			courierRepository.addDelTag(id);

		}
	}

}
