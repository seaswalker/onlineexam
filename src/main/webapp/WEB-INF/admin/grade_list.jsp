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
<title>年级管理</title>
<meta charset="UTF-8">
<base href="<%=basePath%>">
<link rel="SHORTCUT ICON" href="images/icon.ico">
<link rel="BOOKMARK" href="images/icon.ico">
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/head.css">
<link rel="stylesheet" type="text/css" href="css/list_main.css">
<link rel="stylesheet" type="text/css" href="css/modal.css">
</head>
<body>
	<!--头部-->
	<jsp:include page="share/head.jsp"></jsp:include>

	<!--中间主体部分-->
	<div class="main">
		<!--年级-->
		<div class="list" id="grade_list">
			<!--搜索框-->
			<div class="search form-inline">
				<form action="admin/grade/list" method="post" onsubmit="return search(this);">
					<input type="text" class="form-control" name="search" style="width: 300px;">
					&nbsp;&nbsp;
					<button class="btn btn-default" type="submit">搜索</button>
				</form>
			</div>
			<!--操作按钮-->
			<div class="operation_btn">
				<button class="btn btn-success btn-xs" onclick="toggleGradeAdd(true);">添加年级</button>
			</div>
			<table class="table table-hover">
				<thead>
					<tr>
						<th width="20%">id</th>
						<th width="55%">年级</th>
						<th width="25%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageBean.records}" var="grade">
						<tr>
							<td>${grade.id}</td>
							<td>${grade.grade}</td>
							<td>
								<button class="btn btn-danger btn-xs" onclick="deleteGrade(this);">删除</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<!--分页-->
		<div class="page">
			<!-- 用于javascript提交，搜索内容 -->
			<input type="hidden" id="search_content" value="${search}">
			<script type="text/javascript">
				function page(pageCode) {
					var search = document.getElementById("search_content").value;
					window.location.href = "${pageContext.request.contextPath}/admin/grade/list?pn=" + pageCode + "&search=" + search;
				}
			</script>
			<jsp:include page="../share/page.jsp"></jsp:include>
		</div>
	</div>
	
	<!--年级添加-->
	<div class="modal_window major_window form-control" id="grade_add">
		<!--标题-->
		<div class="modal_window_title">
			添加年级: <img src="images/error.png" onclick="toggleGradeAdd(false);">
		</div>
		<form action="grade/add" method="post" onsubmit="return addGrade(this);">
			<table>
				<tr>
					<td>年级名称:</td>
					<td><input type="text" name="grade"></td>
				</tr>
				<tr style="text-align: center;">
					<td colspan="2"><span class="error" id="grade_add_error">&nbsp;</span>
					</td>
				</tr>
				<tr style="text-align: center;">
					<td colspan="2"><input type="submit" value="提交"></td>
				</tr>
			</table>
		</form>
	</div>
</body>
<script type="text/javascript" src="script/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<script src="script/admin/grade.js"></script>
<script src="script/time.js"></script>
<script src="script/tips.js"></script>
</html>