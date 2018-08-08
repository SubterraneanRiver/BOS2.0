package cn.itcast.bos.service.system.imp;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.dao.system.UserRepository;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;


@Service
@Transactional
public class UserServiceImp implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public User findByUsername(String username) {
			return userRepository.findByUsername(username);
	}



	@Override
	public List<User> findAll() {
		
		return userRepository.findAll();
	}



	@Override
	public void save(User user, String[] roleIds) {
		
		userRepository.save(user);
		if(roleIds!=null){
		for (String roleid : roleIds) {
			Role role = roleRepository.findOne(Integer.parseInt(roleid));
			System.out.println("用户名称----"+user.getUsername());
			System.out.println("角色名称:-------"+role.getName());
			
			Set<Role> roles = user.getRoles();
			
			user.getRoles().add(role);
			
			
		}
		}
		
	}



	@Override
	public void delete(String deletID) {
		String[] ids = deletID.split(",");
		
		for (String id : ids) {
			
			userRepository.delete(Integer.parseInt(id));
			
		}
		
		
	}

}
