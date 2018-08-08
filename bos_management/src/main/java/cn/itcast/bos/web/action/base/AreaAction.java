package cn.itcast.bos.web.action.base;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.print.attribute.standard.PageRanges;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.common.BaseAction;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area> {

	@Autowired
    private AreaService areaService;
	
	private File file;
	public void setFile(File file) {
		
		this.file = file;
	}


	//批量区域数据导入
	@Action(value="area_batchImport")
	public String batchImport() throws FileNotFoundException, IOException{
		
		System.out.println("文件对象"+file);
		
		List<Area> areas = new ArrayList<>();
		//加载文件对象
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		//读取每一个sheet
		HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
		//文件导入解析的代码
		//读取sheet中的每一行
		for (Row row : sheet) {
			if(row.getRowNum() == 0)
			{//跳过第一行,第一行通常是标题，需要进行跳过
				continue;
			}
			//跳过空行
			if(row.getCell(0)==null||StringUtils.isBlank(row.getCell(0).getStringCellValue())){
				continue;
			}
			
			//其他是正常的逻辑
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			
			
			//进行城市代码的转换
			String privnce = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			
			privnce = privnce.substring(0, privnce.length()-1);
			city = city.substring(0, city.length()-1);
			district = district.substring(0, district.length()-1);
			
			//取简码
			String[] headArray = PinYin4jUtils.getHeadByString(privnce+city+district);
			StringBuffer buffer = new StringBuffer();
			for (String header : headArray) {
				
				buffer.append(header);
			}
			//将简码拼接成一个字符串
			String shortcode = buffer.toString();
			area.setShortcode(shortcode);
			
			//做城市编码,第二位是分割符合
			String cityCode = PinYin4jUtils.hanziToPinyin(city,"");
			area.setCitycode(cityCode);
			
			//每循环一次加一次	
		   areas.add(area);
			
			}
		
		areaService.saveBatch(areas);
		
		return NONE;
		
		
	}
	
	//分页查询的方法
	@Action(value="area_pageQuery",results={@Result(name="success",type="json")})
	public String PageQuery(){
//查询方法已经将分页的方法进行封装
		//在这里构造条件查询
		Pageable pageable = new PageRequest(page-1, rows);
		//构造条件查询对象
		Specification<Area> specification = new Specification<Area>() {
			
			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				//创建一个保存条件的集合对象
				List<Predicate>list = new ArrayList<Predicate>();
				//添加条件，在这里面root总get到的参数都是实体类的参数
				if(StringUtils.isNoneBlank(model.getProvince())){
					Predicate p1 =cb.like(root.get("province")
							.as(String.class),"%"+model.getProvince()+"%");
					list.add(p1);
				}
				
				//判断市
				if(StringUtils.isNotBlank(model.getCity())){
					
					Predicate p2 = cb.like(root.get("city")
							.as(String.class), "%"+model.getCity()+"%");
				list.add(p2);
				}
				
				//筛选地区
					if(StringUtils.isNotBlank(model.getDistrict())){
					Predicate p3 = cb.like(root.get("district")
							.as(String.class), "%"+model.getDistrict()+"%");
						list.add(p3);
				
					}
				
				//拼接返回条件,这里要对集合赋一个predicate的参数类型
				return cb.and(list.toArray(new Predicate[0]));
			}
		};
		//调用业务层完成查询
		Page<Area>pageData = areaService.findPageData(specification,pageable);
		//将查询的结果压入值栈
		pushPageDataToValueStack(pageData);
		
		return SUCCESS;
	}
	
	
	
}
