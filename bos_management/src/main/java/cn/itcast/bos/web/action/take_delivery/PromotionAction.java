package cn.itcast.bos.web.action.take_delivery;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
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

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;
import cn.itcast.bos.web.action.common.BaseAction;

/*
 * 处理图片保存的action
 * 
 * */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {

	
	//图片使用属性驱动单独处理
	
	private File titleImgFile;
	private String titleImgFileFileName;

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}
@Autowired
private PromotionService promotionService;
	
	
	
	@Action(value = "promotion_save", results = { @Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html")})
	public String manager() throws IOException {

		System.out.println("文件："+titleImgFile);
		System.out.println("输出文件名："+titleImgFileFileName);
		
		// 获取文件的绝对路径
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
		// 获取文件的相对路径
		String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";

		UUID uuid = UUID.randomUUID();
		String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));// 根据最后一个点进行截取
		String randomFileName = uuid + ext;
System.out.println("图片的保存路径"+savePath + File.separator + randomFileName);
		// 保存图片（绝对路径）,直接对路径拷贝
		FileUtils.copyFile(titleImgFile, new File(savePath + File.separator + randomFileName));
		// 通知浏览器文件上传成功
//设置文件的路径和路径名
		model.setTitleImg(ServletActionContext.getRequest().getContextPath()+"/upload/"+randomFileName);
		//将模型进行保存，调用方法
		promotionService.save(model);
		
		return SUCCESS;
	}
	//显示所有的数据信息
	@Action(value="promotion_pageQuery",results={
			@Result(name="success",type="json")
	})
	public String pageQuery(){
		System.out.println("当前页数"+page);
		Pageable pageable = new PageRequest(page-1, rows);//构建页面查询对象 
		//调用业务层完成查询
		 Page<Promotion> pageData = promotionService.finPageData(pageable);
		
		 //这个方法是在baseAction当中的
		 pushPageDataToValueStack(pageData);
		 
		return SUCCESS;
	}
	

}
