package cn.itcast.bos.web.action.system;


import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.UserService;
import cn.itcast.bos.web.action.common.BaseAction;

@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {
	
	@Autowired
	private UserService userService;
	

	@Action(value="user_login",results={
			@Result(name="login",type="redirect",location="login.html"),
			@Result(name="success",type="redirect",location="index.html")
	})
	public String login(){
		
		//基于shiro实现登录
		Subject subject = SecurityUtils.getSubject();
		//用户名和密码信息
		AuthenticationToken token = new UsernamePasswordToken(
				model.getUsername(),model.getPassword());
		
		try {
			//登录成功
			subject.login(token);
			
			
			return SUCCESS;
			
		} catch (AuthenticationException e) {
			//登录失败
			e.printStackTrace();
			return LOGIN;
		}
		
		
		
		
	}
	@Action(value="user_logout",results={
			@Result(name="success",type="redirect",location="login.html")
	})
	public String logout(){
		
		//使用shiro完成退出，这里保存session的工作是由shiro完成的
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		
		return SUCCESS;
	}
	
	
	@Action(value="user_list",results={@Result(
			name="success",type="json")})
	public String findAll(){
		
		List<User> list = userService.findAll();
		
		ActionContext.getContext().getValueStack().push(list);
		
		return SUCCESS;
	}
	
	private String[] roleIds;

	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	//用户信息添加
	@Action(value="user_save",results={@Result(name="success",type="redirect",location="pages/system/userlist.html")})
	public String save(){
	
		for (String id : roleIds) {
			System.out.println("选择的权限"+id);
		}
		
		userService.save(model,roleIds);
		
		return SUCCESS;
		
	}
	//删除用户
	
	private String deletID;
	public void setDeletID(String deletID) {
		this.deletID = deletID;
	}
	@Action(value="user_delete",results={@Result(name="success",type="redirect",location="pages/system/userlist.html")})
	public String delete(){
	
		System.out.println("需要删除的ID"+deletID);
		userService.delete(deletID);
		
		return SUCCESS;
		
	}
}
