<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	pageContext.setAttribute("basePath", basePath);
%>
<html>
<head>
<title>成绩统计</title>
<meta charset="UTF-8">
<base href="<%=basePath%>">
<link rel="SHORTCUT ICON" href="images/icon.ico">
<link rel="BOOKMARK" href="images/icon.ico">
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/head.css">
<link rel="stylesheet" type="text/css" href="css/list_main.css">
<link rel="stylesheet" type="text/css" href="css/teacher/statistics.css">
</head>
<body>
	<!--头部-->
	<jsp:include page="share/head.jsp"></jsp:include>

	<!--等待信息-->
	<div id="wait">
		<input type="hidden" id="eid" value="${eid}" />
		<img src="images/wait.gif">&nbsp;&nbsp;正在统计，请稍候...
	</div>
	<!-- 图示 -->
	<div id="charts" style="display: none;">
		<!-- 试卷题目 -->
		<div id="exam-title"></div>
		<hr>
		<!-- 图片 -->
		<div id="pie">
			<img>
			<!-- 最高分和最低分 -->
			<span style="display: inline-block;margin-left: 50px;margin-top: 20px;width: 300px;">
				<span class="green-rect"></span>
				最高分: &nbsp;&nbsp;<span id="highest-names"></span> <span id="highest-point"></span>分
				<br />
				<br />
				<span class="red-rect"></span>
				最低分: &nbsp;&nbsp;<span id="lowest-names"></span> <span id="lowest-point"></span>分
			</span>
			<div style="position: absolute;left: 900px;top: 500px;">
				<a href="teacher/exam/download/${eid}" target="_blank" class="btn btn-default">生成成绩单</a>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="script/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<script src="script/time.js"></script>
<script src="script/teacher/statistics.js"></script>
</html>