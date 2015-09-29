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
<title>班级管理</title>
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
		<div class="list" id="clazz_list">
			<!--搜索框-->
			<div class="search form-inline">
				<form action="admin/clazz/list" method="post" onsubmit="return search(this);">
					<select name="grade">
						<option value="0">年级...</option>
						<c:forEach items="${grades}" var="grade">
							<option value="${grade.id}">${grade.grade}</option>
						</c:forEach>
					</select>
					<select name="major">
						<option value="0">专业...</option>
						<c:forEach items="${majors}" var="major">
							<option value="${major.id}">${major.name}</option>
						</c:forEach>
					</select>
					<input type="submit" class="btn btn-default btn-xs" value="搜索">
				</form>
			</div>
			<!--操作按钮-->
			<div class="operation_btn">
				<button class="btn btn-success btn-xs" onclick="toggleClazzAdd(true);">添加班级</button>
			</div>
			<table class="table table-hover">
				<thead>
					<tr>
						<th width="15%">id</th>
						<th width="20%">年级</th>
						<th width="30%">专业</th>
						<th width="20%">班级</th>
						<th width="25%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageBean.records}" var="clazz">
						<tr>
							<td>${clazz.id}</td>
							<td>${clazz.grade.grade}</td>
							<td>${clazz.major.name}</td>
							<td>${clazz.cno}</td>
							<td>
								<button class="btn btn-danger btn-xs" onclick="deleteClazz(this);">删除</button>
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
					window.location.href = "${pageContext.request.contextPath}/admin/clazz/list?pn=" + pageCode + "&grade=" + grade + "&major=" + major;
				}
			</script>
			<jsp:include page="../share/page.jsp"></jsp:include>
		</div>
	</div>
	
	<!--班级添加-->
	<div class="modal_window clazz_window form-control" id="clazz_add">
		<!--标题-->
		<div class="modal_window_title">
			添加班级: <img src="images/error.png" onclick="toggleClazzAdd(false);">
		</div>
		<form action="admin/clazz/add" method="post" onsubmit="return addClazz(this);">
			<table>
				<tr>
					<td>所属年级:</td>
					<td>
						<select name="grade" onchange="changeMajor(this);">
							<option value="0">年级...</option>
							<c:forEach items="${grades}" var="grade">
								<option value="${grade.id}">${grade.grade}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td>所属专业:</td>
					<td>
						<!-- 此处由ajax动态加载 -->
						<select id="major_select" name="major">
							<option value="0">专业...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>班号:</td>
					<td><input type="text" name="clazz"></td>
				</tr>
				<tr style="text-align: center;">
					<td colspan="2"><span class="error" id="clazz_add_error">&nbsp;</span>
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
<script src="script/admin/clazz.js"></script>
<script src="script/time.js"></script>
<script src="script/tips.js"></script>
</html>