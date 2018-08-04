package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.constants.Constants;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import freemarker.template.Configuration;
import freemarker.template.Template;

//这里可以引用共同的实体类
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {

	private int page;
	private int rows;

	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	// 查询的方法
	@Action(value = "promotion_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		// 基于webService去获取信息

		@SuppressWarnings("unchecked")
		PageBean<Promotion> pageBean = WebClient.create(Constants.BOS_MANAGEMENT_URL
				+ "/services/promotionService/pageQuery?page=" + page + "&rows=" + rows + "")
				.accept(MediaType.APPLICATION_JSON).get(PageBean.class);
		// 将数据压入
		ActionContext.getContext().getValueStack().push(pageBean);

		return SUCCESS;

	}

	@Action(value = "promotion_showDetail")
	public String showDetail() throws Exception {
		// 先判断所对应的页面是否存在
		String htmlRealPath = ServletActionContext.getServletContext().getRealPath("/freeMarker");// 获得静态页面
		// 合成文件夹路径和文件名称
		File htmlFile = new File(htmlRealPath + "/" + model.getId() + ".html");
		// 如果文件不存在，则需要去数据库查询并生成静态页面
		if (!htmlFile.exists()) {
			Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);// 创建对象引入版本号
			configuration.setDirectoryForTemplateLoading(new File(
					ServletActionContext.getServletContext().getRealPath("/WEB-INF/freeMarker_template")));
					
			// 获取模版对象
			Template template = configuration.getTemplate("promotion_detail.ftl");
			// 获取动态数据
			Promotion promotion = WebClient.create(Constants.BOS_MANAGEMENT_URL
					+ "/bos_management/services/promotionService/promotion/" + model.getId())
					.accept(MediaType.APPLICATION_JSON).get(Promotion.class);
			Map<String, Object> parameterMap = new HashMap<>();
			parameterMap.put("promotion", promotion);
			// 合并输出
			template.process(parameterMap, new FileWriter(htmlFile));
		}
		// 如果不为空,直接将文件写出到输出流

		ServletActionContext.getResponse().setContentType("text/html;charset=utf-8");
		FileUtils.copyFile(htmlFile, ServletActionContext.getResponse().getOutputStream());
		return NONE; // 这里不是要返回一个页面，而是打印一个活动页面的详情

	}

}
