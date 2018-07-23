package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.StandardRepository;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

@Service      //spring的注解
@Transactional
public class StandardServiceImpl implements StandardService {

	// 注入DAO
	@Autowired
	private StandardRepository standardRepository;
	
	@Override
	public void save(Standard standard) {
		standardRepository.save(standard);
	}

	@Override
	public List<Standard> findAll() {
		
	return	standardRepository.findAll();
		
		
	}


	@Override
	//这里的page里既封装了总记录数，又封装了分页的数据信息
	public Page<Standard> findPageData(Pageable pageable) {
		// TODO Auto-generated method stub
		return standardRepository.findAll(pageable);
	}




}
