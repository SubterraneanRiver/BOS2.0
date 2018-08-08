package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.User;

public interface UserService {
//根据姓名查找
	User findByUsername(String username);

	

	List<User> findAll();

	void save(User model, String[] roleIds);



	void delete(String deletID);

	

	
	
}
