package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;
//定区管理
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class FixedAreaAction extends  BaseAction<FixedArea>{
//注入service
	@Autowired
	private FixAreaService fixAreaService;
	//保存定期信息
	@Action(value="fixedArea_save",
			results={@Result(name="success",
			type="redirect",location="./pages/base/fixed_area.html")})
	public String save(){
		fixAreaService.save(model);
		return SUCCESS;
	}
	//查询信息的方法
	@Action(value="fixedArea_pageQuery",
			results={@Result(name="success",type="json")})
	public String pageQuery(){
		Pageable pageable = new PageRequest(page-1, rows);
		
		//构建条件查询对象
		
		Specification<FixedArea> specification = new Specification<FixedArea>() {
			
			@Override
			public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list= new ArrayList<>();
				//构建查询条件
				if(StringUtils.isNotBlank(model.getId())){
					
					Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
					list.add(p1);
				}
				
				if(StringUtils.isNotBlank(model.getCompany())){
					Predicate p2 = cb.like(root.get("company").as(String.class), "%"+model.getCompany()+"%");
					list.add(p2);
				}
				return cb.and(list.toArray(new Predicate[list.size()]));
			}
		};
		
		//调用业务层方法,提供表单信息和分页的信息
		Page<FixedArea> pageData = fixAreaService.findPageData(specification,pageable);
		pushPageDataToValueStack(pageData);
		
		
		return SUCCESS;
		
	}
	//查询未绑定客户的信息
	@Action(value="fixedArea_findNoAssociationCustomer",
			results={@Result(name="success",type="json")})
	public String findNoAssoicationsCustomers(){
		//使用webClient调用wevService接口
		Collection<? extends Customer> collection=WebClient.
				create("http://localhost:8080/crm_management/services/customerService/noassociationcustomers")
				.accept(MediaType.APPLICATION_JSON)
				.getCollection(Customer.class);
		ActionContext.getContext().getValueStack().push(collection);//压入valuestack栈中
		
		return SUCCESS;
	}
	//查询已经绑定的数据的信息
	//查询未绑定客户的信息
		@Action(value="fixedArea_findHasAssociationFixedAreaCustomers",
				results={@Result(name="success",type="json")})
		public String findHasAssoicationFixedAreaCustomers(){
			//使用webClient调用wevService接口
			Collection<? extends Customer> collection=WebClient.
					create("http://localhost:8080/crm_management/services/customerService/associationfixedareacustomers/"+model.getId())
					.accept(MediaType.APPLICATION_JSON)
					.getCollection(Customer.class);
			ActionContext.getContext().getValueStack().push(collection);//压入valuestack栈中
			return SUCCESS;
		}
		//实现绑定功能
		//属性驱动，接收前台提交上来要进行绑定的的数据
		private String[] customerId;
		public void  setCustomerIds(String[] cutomerIds){
			
			this.customerId = cutomerIds;
		}
		//实现关联客户到定区的功能
		@Action(value="fixedArea_associationCustomersToFixedArea",
				results={@Result(name="success",type="redirect",location="./pages/base/fixed_area.html")})
		public String AssoicationCustomersToFixedArea(){
			String customerStr = StringUtils.join(customerId,",");//将数组转换为字符串并通过逗号分割
			WebClient.create("http://localhost:8080/crm_management/services/customerService/"
					+ "associationcustomerstofixedarea?customerIdStr="
					+ customerStr+"&fixedAreaId="+model.getId()).put(null);//拼接字符串，并对结果进行上传
			return SUCCESS;
		}
		
		
	
	
}
