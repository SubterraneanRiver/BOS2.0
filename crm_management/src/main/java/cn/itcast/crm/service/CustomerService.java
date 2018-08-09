package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;
//这里的path不用写了 
public interface CustomerService {

	//查询所有没有进行关联的客户
	@Path("/noassociationcustomers")
	@GET
	@Produces({"application/xml","application/json"})
	public  List<Customer>  findNoAssociationCustomers();
	
	//已经关联到指定定区的客户的列表
	@Path("/associationfixedareacustomers/{fixedareaid}")
	@GET
	@Produces({"application/xml","application/json"})
	public List<Customer> findHasAssciationFixedAreaCustomers(
			@PathParam("fixedareaid") String fixedAreaId);
	
	//将客户关联到定区上，将所有的id拼成123
	@Path("/associationcustomerstofixedarea")
	@PUT
	public void assoicationCustomersToFixedArea(
			@QueryParam("customerIdStr") String customerIdStr,
			@QueryParam("fixedAreaId") String fixedAreaId);
	
	//用户登录信息的验证
	@Path("customer/login")
	@GET
	@Produces({"application/xml","application/json"})
	public Customer login(
			@QueryParam("telephone") String telephone,
			@QueryParam("password") String password);
	
	
	//用户注册的方法
	@Path("customer")
	@POST
	@Consumes({"application/xml","application/json"})
	public void regist(Customer customer);
	
	//查询用户情况
	@Path("customer/telephone/{telephone}")
	@GET
	@Consumes({"application/xml","application/json"})
	public Customer findByTelephone(@PathParam("telephone") String telephone);
	
	@Path("customer/updatetype/{telephone}")
	@GET
	public void updataType(@PathParam("telephone") String telephone);
	
}
