package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.management.RuntimeErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

import cn.itcast.bos.domain.constants.Constants;
import cn.itcast.bos.utils.MailUtils;
import cn.itcast.bos.utils.SmsDayuUtils;
import cn.itcast.crm.domain.Customer;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CustomerAction extends BaseAction<Customer> {

	//注入redis的模版
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	//注入activeMq的模块
	@Autowired
	@Qualifier("jmsQueueTemplate")
	private JmsTemplate jmsTemplate;
	

	// 用户注册功能的实现
	@Action(value = "customer_sendSms")
	public String sendSms() throws Exception {
		// 手机号保存在Customer对象
		// 生成短信验证码4位
		final String randomCode = RandomStringUtils.randomNumeric(4);
		System.out.println("生成的短信验证码为" + randomCode);
		//将验证码存入到session中等待短信验证
		ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), randomCode);
		
		//调用activeMQ服务发出短信
		jmsTemplate.send("bos_sms",new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telephone",model.getTelephone());
				mapMessage.setString("randomCode", randomCode);
				
				return mapMessage;
			}
		});
		
		return NONE;
		
		//这里是发短信的逻辑
		/*SendSmsResponse response = SmsDayuUtils.sendSms(model.getTelephone(), randomCode);
		String result  = response.getCode();
		//String result = "ok";
		String message = response.getMessage(); 
		//String message = "短信发送成功";
		// 短信调用成功的逻辑
		if ("OK".equalsIgnoreCase(result)) {
			System.out.println("短信的发送结果" + message);
			// 在这里不用返回值
			
		} else {
			throw new RuntimeException("短信发生失败,信息码为" + message);
		}*/

		
		
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
	
	//属性驱动接收验证码
	private String checkcode;
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	// 用户注册的方法
	@Action(value="customer_regist",results={@Result(name="success",type="redirect",location="signup-success.html")
	,@Result(name="input",type="redirect",location="signup.html")})
	public String regist(){
		
		//Customer customer = new Customer();
		//先校验短信验证码，从session获取之前生成的验证码;
		
		System.out.println(model.toString());
		
		
		String checkcodeSession = (String) ServletActionContext.getRequest()
				.getSession().getAttribute(model.getTelephone());
		
		System.out.println("输入的验证码"+checkcode);
		
		
		if(checkcodeSession == null|| !checkcodeSession.equals(checkcode)){
			System.out.println("短信验证码错误");
			return INPUT;
			
		}else{
			
			//调用webService连接CRM保存客户信息
			WebClient
			.create(Constants.CRM_MANAGMENT_URL+"/services/customerService/customer")
			.type(MediaType.APPLICATION_JSON).post(model);
			//调用crm系统完成保存操作
			System.out.println("客户注册成功....");
			
			//步骤2发送一封激活邮件
			//生成一个32位激活码
			String activecodes=RandomStringUtils.randomNumeric(32);
			System.out.println("生成的激活码"+activecodes);
			//将激活码保存到redis中，并设置24小时失效
			redisTemplate.opsForValue().set(model.getTelephone(), activecodes,24,TimeUnit.HOURS);
			String content="<h3>尊敬的客户你好，请于24小时内点击邮箱地址进行点击绑定:<a href=" + MailUtils.activeUrl
					+ "?activecode=" + activecodes + "&telephone="+model.getTelephone()+">速运快递激活地址</a></h3>";
			//发送邮件
			
			MailUtils.sendMail("速运快递", content, model.getEmail());
			
			
			return SUCCESS;
		}
		
	}
//属性注入获得激活码
	private String activecode;
	//邮箱激活
	
	public void setActivecode(String activecode) {
		this.activecode = activecode;
	}


	@Action(value="customer_activeMail")
	public String activeMail() throws IOException{
		
		System.out.println("到达active_mail方法");
		System.out.println("接收到的激活码1："+activecode);
		//ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		ServletActionContext.getResponse().setContentType(
				"text/html;charset=utf-8");
		
		//判断激活码是否有效,根据电话号码来获得
		//String activecodeRedis= redisTemplate.opsForValue().get(model.getTelephone());
		String activecodeRedis = redisTemplate.opsForValue().get(
				model.getTelephone());
		
		
		System.out.println("接收到的激活码2："+activecode);
		System.out.println("redis中的激活码"+activecodeRedis);
		
		
		if(activecodeRedis==null||!activecodeRedis.equals(activecode)){
			
			ServletActionContext.getResponse().getWriter().println("激活码无效,请从新登录系统,从新绑定邮箱");
			
		}else{
			//调用webservice接口改状态
			Customer customer = WebClient.
			create(Constants.CRM_MANAGMENT_URL+"/services/customerService/customer/telephone/"+model.getTelephone())
			.accept(MediaType.APPLICATION_JSON).get(Customer.class);
			//根据返回的结果进行处理
			if(customer.getType()==null||customer.getType()!=1){
				//邮箱还没有绑定，进行绑定
				WebClient.create(
						Constants.CRM_MANAGMENT_URL+"/services/customerService/customer/updatetype/"
				+model.getTelephone()).get();//这里发get请求，返回空值
				ServletActionContext.getResponse().getWriter().println("邮箱绑定成功！！！");
			}else{
				
				ServletActionContext.getResponse().getWriter().println("邮箱已经激活，无需重复绑定");
			}
			
			redisTemplate.delete(model.getTelephone());
		}
		
		return NONE;
		
	}
	
	
	
}
