package cn.itcast.bos.web.action.system;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
import cn.itcast.bos.web.action.common.BaseAction;
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class MenuAction extends BaseAction<Menu> {

	@Autowired
	private MenuService menuService;
	
	
	@Action(value="menu_list",results={@Result(name="success",type="json")})
	public String list(){

		List<Menu> list = menuService.findAll();
		
		ActionContext.getContext().getValueStack().push(list);
		
		return SUCCESS;
		
	}
	
	
//保存菜单信息	
	@Action(value="menu_save",results={@Result(
			name="success",type="redirect",location="pages/system/menu.html")})
			//name="success",type="chain",location="menu_list")})//直接在服务器内部转发到查询页面
	public String menuSave(){
		menuService.save(model);
		
		//这里要回到原来的页面
		return SUCCESS;
	}

	//动态加载菜单的信息

	@Action(value="menu_showmenu",results={@Result(name="success",type="json")})
	public String showMenu(){

		//首先查询当前登录用户的信息
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		
		List<Menu> list = menuService.findByUser(user);
		
		ActionContext.getContext().getValueStack().push(list);
		
		return SUCCESS;
		
	}
	
}
