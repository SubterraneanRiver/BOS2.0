package cn.itcast.bos.quartz;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.dao.take_delivery.PromotiomRepository;
import cn.itcast.bos.service.take_delivery.PromotionService;

public class PromotionJob implements Job {
@Autowired
	private PromotionService promotionService;
	
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		System.out.println("活动过期处理程序执行。。。。。。。");
		
		promotionService.updateStatus(new Date());
		
		
	}

	
	
	
	
}
