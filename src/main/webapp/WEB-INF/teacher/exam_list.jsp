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
<title>试卷</title>
<meta charset="UTF-8">
<base href="<%=basePath%>">
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/head.css">
<link rel="stylesheet" type="text/css" href="css/list_main.css">
<link rel="stylesheet" type="text/css" href="css/modal.css">
<script type="text/javascript" src="script/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<script src="script/teacher/exam.js"></script>
<script src="script/time.js"></script>
<script src="script/tips.js"></script>
</head>
<body>
	<!--头部-->
	<jsp:include page="share/head.jsp"></jsp:include>

	<!--中间主体部分-->
	<div class="main">
		<!--年级-->
		<div class="list" id="clazz_list">
			<!--搜索框-->
			<div class="search form-inline">
				
			</div>
			<table class="table table-hover">
				<thead>
					<tr>
						<th width="15%">id</th>
						<th width="25%">标题</th>
						<th width="30%">适用班级</th>
						<th width="15%">状态</th>
						<th width="25%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageBean.records}" var="exam">
						<tr>
							<td>${exam.id}</td>
							<td>${exam.title}</td>
							<td><a href="javascript:showClazz();">显示</a></td>
							<td>${exam.close ? '关闭' : '运行'}</td>
							<td>
								<button class="btn btn-danger" onclick="deleteExam(this);">删除</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<!--分页-->
		<div class="page">
			<!-- 用于javascript提交，搜索内容 -->
			<input type="hidden" id="search_grade" value="${grade}">
			<input type="hidden" id="search_major" value="${major}">
			<script type="text/javascript">
				function page(pageCode) {
					var grade = document.getElementById("search_grade").value;
					var major = document.getElementById("search_major").value;
					window.location.href = "admin/clazz/list?pn=" + pageCode + "&grade=" + grade + "&major=" + major;
				}
			</script>
			<jsp:include page="../share/page.jsp"></jsp:include>
		</div>
	</div>
	
	<!--班级添加-->
	<div class="modal_window clazz_window form-control" id="clazz_add">
		<!--标题-->
		<div class="modal_window_title">
			适用的班级: <img src="images/error.png" onclick="toggleClazzAdd(false);">
		</div>
	</div>
</body>
</html>