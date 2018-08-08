package cn.itcast.bos.service.base.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
@Service
@Transactional
public class MenuServiceimp implements MenuService {

	@Autowired
	private MenuRepository menuRepository; 
	
	@Override
	public List<Menu> findAll() {
		
		return menuRepository.findAll();
	}

	@Override
	public void save(Menu model) {
		
		//处理父菜单为空或者为0时的情况
		if(model!=null && model.getParentMenu().getId()==0)
			model.setParentMenu(null);
		menuRepository.save(model);
		
	}

	@Override
	public List<Menu> findByUser(User user) {
		
		
		if(user.getUsername().equals("admin")){
		
			return menuRepository.findAll();
	
		}else{
			
			List<Menu> list = menuRepository.findByUser(user.getId());
			for (Menu menu : list) {
				System.out.println("寻找到的菜单"+menu.getName());
			}
			
			
			return menuRepository.findByUser(user.getId());
		}
		
		
		
		}

	
	
}
