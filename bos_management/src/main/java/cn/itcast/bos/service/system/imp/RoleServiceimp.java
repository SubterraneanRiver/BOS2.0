package cn.itcast.bos.service.system.imp;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.RoleService;

@Service
@Transactional
public class RoleServiceimp implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PermissionRepository permissionRepository;
	@Autowired
	private MenuRepository menuRepository;
	
	
	@Override
	public List<Role> findByUser(User user) {
		
		//让admin具有所有的权限
		if(user.getUsername().equals("admin"))
		{
			return roleRepository.findAll();
		}else{
			
			return roleRepository.findByUser(user.getId());
		}
		
		
	}


	@Override
	public List<Role> findAll() {
		
		return roleRepository.findAll();
	}


	@Override
	public void saveRole(Role role, String[] permissionIds, String menuIds) {
		
		//保存角色信息,保存后会自动返回一个保存了的id
		roleRepository.save(role);
		//关联权限信息
		if(permissionIds != null ){
			
			for (String permissionid : permissionIds) {
				Permission permission = permissionRepository.findOne(Integer.parseInt(permissionid));
				
				role.getPermissions().add(permission);
			}
		}
		
		//关联菜单信息
		if(StringUtils.isNotBlank(menuIds)){
			String[] menuidArray = menuIds.split(",");
			for (String menuid : menuidArray) {
				
				Menu menu = menuRepository.findOne(Integer.parseInt(menuid));
				role.getMenus().add(menu);
			}
			
		}
		
	}




}
