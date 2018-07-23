package cn.itcast.bos.web.action.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@ParentPackage("json-default")
@Namespace("/")//struts2的注解
@Controller    //spring的注解将action交给spring管理
@Scope("prototype")   //spring的注解,设置为多例
public class StandardAction extends ActionSupport implements
		ModelDriven<Standard> {

	// 模型驱动
	private Standard standard = new Standard();

	@Override
	public Standard getModel() {
		return standard;
	}

	// 注入Service对象
	@Autowired
	private StandardService standardService;

	// 分页列表查询显示数据
	//查找取件方式的findAll方法
	@Action(value="standard_findAll",
			results={@Result(name="success",type="json")})
	public String findAll(){
		//无条件查询
	List<Standard> standards = standardService.findAll();
		
	ActionContext.getContext().getValueStack().push(standards);
		return SUCCESS;
	}
	
	// 添加和修改操作
	@Action(value = "standard_save", results = { @Result(name = "success", type = "redirect", location = "./pages/base/standard.html") })
	public String save() {
		System.out.println("添加收派标准....");
		standardService.save(standard);
		return SUCCESS;
	}
	// 属性驱动
	private int page;
	private int rows;
//使用属性驱动接收数据
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	//处理查询分页请求
	@Action(value="standard_pageQuery",results={@Result(name="success",
			type="json")})
public String pageQuery(){
		//调用业务层查询总记录数
		
		
	//调用业务层查询数据结果
		Pageable pageable = new PageRequest(page-1, rows);
		//这里page对象返回的数据包含了总页数，和总的记录数，和内容，所有的信息这是由spring框架提供的功能
		Page<Standard> pageData = standardService.findPageData(pageable);
	Map<String, Object> result = new HashMap<>();
	//result.put("total",pageData.getNumberOfElements());
	System.out.println("numberOfelements的值"+pageData.getNumberOfElements());
	result.put("total",pageData.getTotalElements());//这里应该用totalofElecmets才是查询的总记录数
	result.put("rows", pageData.getContent());//返回当前的内容
		
	//将map转换为json数据，将数据压人值栈的顶部
	 
	ActionContext.getContext().getValueStack().push(result);
	
	return SUCCESS;
	
}
	

}
