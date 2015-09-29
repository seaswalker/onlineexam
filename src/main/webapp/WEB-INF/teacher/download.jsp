<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	pageContext.setAttribute("basePath", basePath);
%>
<html>
<head>
<title>文件下载</title>
<meta charset="UTF-8">
<base href="<%=basePath%>">
<link rel="SHORTCUT ICON" href="images/icon.ico">
<link rel="BOOKMARK" href="images/icon.ico">
<link rel="stylesheet" type="text/css" href="css/head.css">
<style type="text/css">
	.main {
		width: 980px;
		margin: 0px auto 0px;
		height: 300px;
	}
</style>
</head>
<body>
	<!--头部-->
	<div class="head">
		<div class="head_logo">
			<img src="images/logo.png"> <span class="logo_content">欢迎使用考试系统</span>
		</div>
		<!--显示登录的用户-->
		<div class="head_info"></div>
		<hr />
	</div>

	<div class="main">
		您的文件正在火速赶来的路上，请稍候...
		<form action="teacher/exam/report/${eid}" id="download-form"></form>
	</div>
</body>
<script type="text/javascript">
	//发送下载请求
	window.onload = function() {
		document.getElementById("download-form").submit();
	};
</script>
</html>