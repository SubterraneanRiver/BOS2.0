package cn.itcast.bos.service.take_delivery;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;

public interface PromotionService {
//保存宣传任务
	void save(Promotion model);
//分页查询
	Page<Promotion> finPageData(Pageable pageable);

	//根据page和rows返回分页数据,这是rs的注解，webservice的接口
	@Path("/pageQuery")
	@GET  //配置请求方式
	@Produces({"application/xml","application/json"})
	PageBean<Promotion>findPageData(@QueryParam("page") int page,@QueryParam("rows") int rows);
	
//根据idc查询	
	
	@Path("/promotion/{id}")
	@GET
	@Produces({ "application/xml", "application/json" })
	Promotion findById(@PathParam("id") Integer id);
	void updateStatus(Date date);

	
	/*@Path("/promotion/{id}")
	@GET
	@Produces({"application/xml","application/json"})
	Promotion findById(@PathParam("id") Integer id);*/
	
	
	
	
	
}
