package cn.itcast.bos.web.action;

import javax.management.RuntimeErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

import cn.itcast.bos.domain.constants.Constants;
import cn.itcast.bos.utils.SmsDayuUtils;
import cn.itcast.crm.domain.Customer;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {

	// 用户注册功能的实现
	@Action(value = "customer_sendSms")
	public String sendSms() throws Exception {
		// 手机号保存在Customer对象
		// 生成短信验证码4位
		String randomCode = RandomStringUtils.randomNumeric(4);

		System.out.println("生成的短信验证码为" + randomCode);

		//将验证码存入到session中等待短信验证
		/*ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), randomCode);
		SendSmsResponse response = SmsDayuUtils.sendSms(model.getTelephone(), randomCode);
		String result  = response.getCode();*/
		String result = "ok";
		//String message = Response.getMessage(); 
		String message = "短信发送成功";
		// 短信调用成功的逻辑
		if ("OK".equalsIgnoreCase(result)) {
			
			System.out.println("短信的发送结果" + message);
			// 在这里不用返回值
			return NONE;
		} else {
			throw new RuntimeException("短信发生失败,信息码为" + message);
		}

	}

	// 实现用户登录功能
	@Action(value = "customer_login", results = { @Result(name = "login", location = "login.html", type = "redirect"),
			@Result(name = "success", location = "index.html#/myhome", type = "redirect") })
	public String login() {
		// 通过webservice获取登录数据信息
		Customer customer = WebClient
				.create(Constants.CRM_MANAGMENT_URL + "/services/customerService/customer/login?telephone="
						+ model.getTelephone() + "&password=" + model.getPassword())
				.accept(MediaType.APPLICATION_JSON).get(Customer.class);
		// 判断查询到的用户
		if (customer == null) {
			return LOGIN;
		} else {
			ServletActionContext.getRequest().getSession().setAttribute("customer", customer);
			return SUCCESS;
		}

	}

	// 上面一个功能结束

}
