<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><s:text name="system.title" /></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />


<script type="text/javascript">
	
</script>
</head>
<body>

<h2>未捕获的异常</h2>
<p>存在未捕获的程序异常，可能是网站正在升级中，请稍后再试……</p>
<hr />
<h3>错误信息</h3>
<s:actionerror />
<p><s:property value="%{exception.message}" /></p>
<hr />
<h3>详细内容</h3>
<p><s:property value="%{exceptionStack}" /></p>
<s:debug/>
</body>
</html>