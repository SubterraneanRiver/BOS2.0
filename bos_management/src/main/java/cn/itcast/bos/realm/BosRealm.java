package cn.itcast.bos.realm;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.service.system.UserService;

//自定义realm
//@Service("bosRealm")
public class BosRealm extends AuthorizingRealm {
	
	
	@Autowired
	private UserService usersrvice; 
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permisssionService; 
	
	
	
//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		
		System.out.println("shiro的授权管理");
		
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		//根据当前登录的用户查询角色权限
		
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		//调用业务层c查询角色
		
		List<Role> roles = roleService.findByUser(user);
		
		for (Role role : roles) {
			
			authorizationInfo.addRole(role.getKeyword());
			
		}
		//查询权限
		List<Permission> permissions = permisssionService.findByUser(user);
for (Permission permission : permissions) {
authorizationInfo.addStringPermission(permission.getKeyword());	
}		
		return authorizationInfo;
	}
//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("shiro的认证管理");
		
		
		UsernamePasswordToken usernamePasswordToken =  (UsernamePasswordToken) token;
		
		User user = usersrvice.findByUsername(usernamePasswordToken.getUsername());
		
		if(user==null){
			return null;
				
		}else{
			
			return new SimpleAuthenticationInfo(user, user.getPassword()
					,getName());
			
		}
		
	}
	
}
