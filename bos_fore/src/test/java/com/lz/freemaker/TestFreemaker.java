package com.lz.freemaker;

import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreemaker {

	@Test
	public void t1() throws Exception {
		
		//配置对象，配置模版位置
		Configuration configuration = new Configuration(
				Configuration.VERSION_2_3_22);
		configuration.setDirectoryForTemplateLoading(new File(
				"src/main/webapp/WEB-INF/template"));
		
		//获取模版对象
		Template template = configuration.getTemplate("hello.ftl");
		
		//动态数据对象
		Map<String, Object> paramterMap = new HashMap<>();
		paramterMap.put("title", "测试案例");
		paramterMap.put("msg", "这是一个freemaker的测试程序");
		//合并输出
		template.process(paramterMap, new PrintWriter(System.out));
		
	}
	
	
}
