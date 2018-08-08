package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.PermissionService;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PermissionAction extends BaseAction<Permission>{

	
	@Autowired
	private PermissionService permissionService; 
	
	
	
	@Action(value="permission_list",results={@Result(name="success"
			,type="json")})
	public String permissonList(){
		
		List<Permission> list= permissionService.findAll();
		
		ActionContext.getContext().getValueStack().push(list);
		
		return SUCCESS;
		
	}
	
	//添加权限的信息
	
		@Action(value="permission_save",results={@Result(
				name="success",type="redirect",location="pages/system/permission.html")})
				//name="success",type="chain",location="menu_list")})//直接在服务器内部转发到查询页面
		public String menuSave(){
			permissionService.save(model);
			
			//这里要回到原来的页面
			return SUCCESS;
		}
	
	
}
