package com.lz.bos.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.lz.bos.utis.SmsDayuUtils;

@Service("smsCustomer")
public class SmsCustomer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		//当收到消息的时候
		MapMessage mapMessage = (MapMessage) message;
		
		try {
			//调用sms发送短信
			SendSmsResponse response = SmsDayuUtils.sendSms(mapMessage.getString("telephone")
					, mapMessage.getString("randomCode"));
			String result  = response.getCode();
			//String result = "ok";
			String msg = response.getMessage(); 
			//String msg = "短信发送成功";
			// 短信调用成功的逻辑
			if ("OK".equalsIgnoreCase(result)) {
				System.out.println("短信的发送结果" + msg);
				// 在这里不用返回值
				
			} else {
				throw new RuntimeException("短信发生失败,信息码为" + message);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
	}

	
	
}
