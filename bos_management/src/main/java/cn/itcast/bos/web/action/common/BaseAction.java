package cn.itcast.bos.web.action.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

	//模型驱动
	protected T model;
	
	@Override
	public T getModel(){
		return model;
	}
	//构造器 完成modelde的实例化,这一段很重要，是通过类加载器进行的加载
	public BaseAction() {
		
		Type genericSupercalss = this.getClass().getGenericSuperclass();
		
		//获取类型的第一个泛型参数
		ParameterizedType parameterizedType = (ParameterizedType) genericSupercalss;
		
		Class<T> modelClass = (Class<T>)parameterizedType.getActualTypeArguments()[0];
		
			try {
				model = modelClass.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("模型构造失败");
			}
	}
	//接收查询分页参数
	protected int page;
	protected int rows;

	public void setPage(int page) {
		this.page = page;
	}



	public void setRows(int rows) {
		this.rows = rows;
	}
	
	//将分页查询的结果压入值栈
	protected void pushPageDataToValueStack(Page<T> pageData){
		Map<String ,Object> result=new HashMap<>();
		
		result.put("total", pageData.getTotalElements());
		result.put("rows", pageData.getContent());
		ActionContext.getContext().getValueStack().push(result);
		
	}
	
	
}
