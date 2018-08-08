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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.web.action.common.BaseAction;

/*
 * 处理kindeditor的图片请求
 * */

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ImageAction extends BaseAction<Object> {

	private File imgFile;
	private String imgFileFileName;
	private String imgFileContentType;
	
	//使用属性驱动，提供set方法
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}

	public void setImgFileFileName(String imgFileFileName) {
		this.imgFileFileName = imgFileFileName;
	}

	public void setImgFileContentType(String imgFileContentType) {
		this.imgFileContentType = imgFileContentType;
	}
	
	@Action(value="image_upload",results={
			@Result(name="success",type="json")})
	public String upload() throws Exception{
		System.out.println("文件："+imgFile);
		System.out.println("文件名"+imgFileFileName);
		System.out.println("文件类型"+imgFileContentType);
		
		//获取文件的绝对路径
		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
		//获取文件的相对路径
		String saveUrl = ServletActionContext.getRequest().getContextPath()+"/upload/";
		//生产随机图片名称
		UUID uuid = UUID.randomUUID();
		String ext = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));//根据最后一个点进行截取
		String randomFileName = uuid+ext;
		
		//保存图片（绝对路径）,直接对路径拷贝
		FileUtils.copyFile(imgFile, new File(savePath+"/"+randomFileName));
		//通知浏览器文件上传成功
		
		
		//通过map进行转换
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("error", 0);
		result.put("url", saveUrl+randomFileName);
		//将结果压入到值栈中
		ActionContext.getContext().getValueStack().push(result);
		return SUCCESS;
	}

	//显示图片空间
	@Action(value="image_manager",results={
			@Result(name="success",type="json")
	})
	public String manager(){
		
		
		String rootPath = ServletActionContext.getServletContext().getRealPath("/") + "upload/";
		//根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
		String rootUrl  =  ServletActionContext.getRequest().getContextPath() + "/upload/";
//创建一个真实路径的文件对象
		File currentPathFile = new File(rootPath);
		//图片扩展名
		String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};
		//遍历目录取的文件信息
		List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
		if(currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<String, Object>();
				String fileName = file.getName();
				if(file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if(file.isFile()){
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("moveup_dir_path", "");
		result.put("current_dir_path", rootPath);
		result.put("current_url", rootUrl);
		result.put("total_count", fileList.size());
		result.put("file_list", fileList);
		ServletActionContext.getContext().getValueStack().push(result);
		
		return SUCCESS;
	}
	
}
