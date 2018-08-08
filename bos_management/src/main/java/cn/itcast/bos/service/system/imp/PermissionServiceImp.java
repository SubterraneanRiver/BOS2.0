package cn.itcast.bos.service.system.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;

@Service
@Transactional

public class PermissionServiceImp implements PermissionService{

	@Autowired
	private PermissionRepository  permissionRepository;
	
	
	@Override
	public List<Permission> findByUser(User user) {
		
		if(user.getUsername().equals("admin")){
			
			return permissionRepository.findAll();
		}else{
			//根据用户进行查询
			return permissionRepository.findByUser(user.getId());
			
		}
		
		
	}


	@Override
	public List<Permission> findAll() {
		
		return permissionRepository.findAll();
	}


	@Override
	public void save(Permission permission) {
		
		permissionRepository.save(permission);
		
	}

	
	
}
