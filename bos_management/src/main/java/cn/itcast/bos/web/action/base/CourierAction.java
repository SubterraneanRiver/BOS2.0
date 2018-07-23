package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
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
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {

	// 定义一个courier用来做模型驱动
	private Courier courier = new Courier();

	@Override
	public Courier getModel() {
		// TODO Auto-generated method stub
		return courier;
	}

	// 进行service注入
	@Autowired
	private CourierService courierService;

	// 保存快递员信息的方法
	@org.apache.struts2.convention.annotation.Action(value = "courier_save", results = {
			@Result(name = "success", location = "./pages/base/courier.html", type = "redirect") })
	public String save() {
		courierService.save(courier);
		return SUCCESS;
	}

	// 实现无条件查询分页,使用属性驱动
	private int page; // 页数
	private int rows;// 页面行数

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Action(value = "courier_pageQuery", results = { @Result(name = "success", type = "json") }) // 这里只需要得到返回的数据即可
	public String pageQuery() {
		// 封装pageable对象
		System.out.println("页面码" + page);
		Pageable pageable = new PageRequest(page - 1, rows);// 前台页面是从1开始计算的选哟减去1
		// 根据条件查询构造specification
		Specification<Courier> specification = new Specification<Courier>() {
			@Override
			// 构造条件查询方
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<>();
				if (StringUtils.isNotBlank(courier.getCourierNum())) {
					Predicate p1 = cb.equal(root.get("courierNum"), courier.getCourierNum());
					list.add(p1);
				}
				if (StringUtils.isNotBlank(courier.getCompany())) {
					Predicate p2 = cb.like(root.get("company").as(String.class), "%" + courier.getCompany() + "%");
					list.add(p2);
				}

				if (StringUtils.isNotBlank(courier.getType())) {

					Predicate p3 = cb.equal(root.get("type").as(String.class), courier.getType());
					list.add(p3);
				}
				// 多表查询（查询当前对象，关联对象对应的数据）
				// 使用courier（root）,关联standard
				Join<Object, Object> standardRoot = root.join("standard", JoinType.INNER);// 这句话是什么鬼，还要再研究一下，用来关联standard的写法

				if (courier.getStandard() != null && StringUtils.isNotBlank(courier.getStandard().getName())) {

					// 进行收派标准模糊查询

					Predicate p4 = cb.like(standardRoot.get("name").as(String.class),
							"%" + courier.getStandard().getName() + "%");
					list.add(p4);
				}
				return cb.and(list.toArray(new Predicate[0]));//new predicate起到泛型的作用构造数组
			}
		};

		// 调用业务层
		Page<Courier> pageData = courierService.findpageData(specification, pageable);
		Map<String, Object> result = new HashMap<>();
		result.put("total", pageData.getTotalElements());// 总记录数
		result.put("rows", pageData.getContent());// 获取当前页面
		ActionContext.getContext().getValueStack().push(result);// 将数据值栈，转换为json格式
		return SUCCESS;
	}

	//快递员的作废功能实现
	
	private String ids;
	
	
	public void setIds(String ids) {
		this.ids = ids;
	}

	@Action(value="courier_delBatch",results={@Result(name="success",
			location="./pages/base/courier.html",type="redirect")})
	public String delBatch(){
		//按逗号分割ids
		String[] idArray = ids.split(",");
		//进行批量作废
		courierService.delBatch(idArray);
		return SUCCESS;
	}
	
	//快递员状态的还原，courier_addBatch
	@Action(value="courier_addBatch",results={@Result(name="success",
			location="./pages/base/courier.html",type="redirect")})
	public String addBatch(){
		//按逗号分割ids
		String[] idArray = ids.split(",");
		//进行批量作废
		courierService.addBatch(idArray);
		return SUCCESS;
	}
	
	
}
