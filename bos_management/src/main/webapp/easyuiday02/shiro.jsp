<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>测试shiro标签的使用</title>
        	<script type="text/javascript" src="../js/jquery-1.8.3.js"></script>
		<!-- 导入easyui类库 -->
		<link id="easyuiTheme" rel="stylesheet" type="text/css" href="../js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="../js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="../css/default.css">
		<script type="text/javascript" src="../js/easyui/jquery.easyui.min.js"></script>
    </head>
    <body>
    <shiro:hasPermission name="courier:add">
    <a href="#" class="easyui-linkbutton">添加</a>
    </shiro:hasPermission>
    
    	<shiro:hasPermission name="courier:edit">
    	<a href="#" class="easyui-linkbutton">修改</a>
    	 </shiro:hasPermission>
    	<shiro:hasPermission name="courier:delete">
    	<a href="#" class="easyui-linkbutton">删除</a>
    	 </shiro:hasPermission>
    	<shiro:hasPermission name="courier:list">
    	<a href="#" class="easyui-linkbutton">查询</a>
    	 </shiro:hasPermission>
    	
 	</body>
 	
 	
</html>