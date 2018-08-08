package cn.itcast.bos.service.take_delivery.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.take_delivery.PromotiomRepository;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;

@Service
@Transactional
public class PromotionServiceimp implements PromotionService{

	@Autowired
	private PromotiomRepository promotiomRepository;
	
	@Override
	public void save(Promotion model) {
		
		promotiomRepository.save(model);
		
		
	}

	@Override
	public Page<Promotion> finPageData(Pageable pageable) {
		//无条件分页查询
		
		return promotiomRepository.findAll(pageable);
	}

	//进行分页查找数据的实现
	@Override
	public PageBean<Promotion> findPageData(int page, int rows) {
		Pageable pageable = new PageRequest(page-1, rows);
		Page<Promotion> pageData =promotiomRepository.findAll(pageable);
 		
		//封装到page
		PageBean<Promotion> pageBean  = new PageBean<Promotion>();
		pageBean.setTotalCount(pageData.getTotalElements());
		pageBean.setPageData(pageData.getContent());
		
		
		return pageBean;
	}

	
	//处理根据id查找的办法
	@Override
	public Promotion findById(Integer id) {
		System.out.println("传上来的id"+id);
		return promotiomRepository.findOne(id);
	}

	@Override
	public void updateStatus(Date date) {
		
		promotiomRepository.updateStatus(date);
		
	}
	
	/*@Override
	public Promotion findById(Integer id) {
		
		System.out.println("传上来的id"+id);
		return promotiomRepository.findOne(id);
	}*/

}
