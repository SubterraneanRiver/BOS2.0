package cn.itcast.bos.domain.constants;
//系统子自定义的常量类
public class Constants {

	//这个跑的tomcat7：run
	private static final String BOS_MANAGEMENT_HOST = "http://localhost:8088";
	//这个跑的tomcat:run
	private static final String CRM_MANAGMENT_HOST = "http://localhost:9002";
	
	private static final String BOS_FORE_HOST="http://localhost:9003";
	
	
	
	private static final String BOS_MANAGEMENT_CONTEXT ="/bos_management";
	
	private static final String CRM_MANAGEMENT_CONTEXT ="/crm_management";
	
	private static final String BOS_FORE_CONTEXT ="/bos_fore";
	
	
	
	public static final String BOS_MANAGEMENT_URL = BOS_MANAGEMENT_HOST+BOS_MANAGEMENT_CONTEXT;
	public static final String CRM_MANAGMENT_URL = CRM_MANAGMENT_HOST+CRM_MANAGEMENT_CONTEXT;
	public static final String BOS_FORE_URL = BOS_FORE_HOST+BOS_FORE_CONTEXT;
}
