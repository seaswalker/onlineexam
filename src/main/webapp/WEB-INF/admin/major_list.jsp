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
<title>主页</title>
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
		<!--专业-->
		<div class="list" id="major_list">
			<!--搜索框-->
			<div class="search form-inline">
				<form action="admin/major/list" method="post" onsubmit="return search(this);">
					<input type="text" class="form-control" name="search" style="width: 300px;">
					&nbsp;&nbsp;
					<button class="btn btn-default" type="submit">搜索</button>
				</form>
			</div>
			<!--操作按钮-->
			<div class="operation_btn">
				<button class="btn btn-success btn-xs" onclick="toggleMajorAdd(true);">添加专业</button>
			</div>
			<table class="table table-hover">
				<thead>
					<tr>
						<th width="15%">id</th>
						<th width="60%">专业名称</th>
						<th width="20%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageBean.records}" var="major">
						<tr>
							<td>${major.id}</td>
							<td>${major.name}</td>
							<td>
								<button class="btn btn-default btn-xs" onclick="toggleMajorEdit(true, this);">编辑</button>
								<button class="btn btn-danger btn-xs" onclick="deleteMajor(this);">删除</button>
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
					window.location.href = "${pageContext.request.contextPath}/admin/major/list?pn=" + pageCode + "&search=" + search;
				}
			</script>
			<jsp:include page="../share/page.jsp"></jsp:include>
		</div>
	</div>
	
	<!--专业添加-->
	<div class="modal_window grade_window form-control" id="major_add">
		<!--标题-->
		<div class="modal_window_title">
			添加专业: <img src="images/error.png" onclick="toggleMajorAdd(false);">
		</div>
		<form action="major/add" method="post" onsubmit="return addMajor(this);">
			<table>
				<tr>
					<td>专业名称:</td>
					<td><input type="text" name="major"></td>
				</tr>
				<tr style="text-align: center;">
					<td colspan="2"><span class="error" id="major_add_error">&nbsp;</span>
					</td>
				</tr>
				<tr style="text-align: center;">
					<td colspan="2"><input type="submit" value="提交"></td>
				</tr>
			</table>
		</form>
	</div>
	<!--专业修改-->
	<div class="modal_window grade_window form-control" id="major_edit">
		<!--标题-->
		<div class="modal_window_title">
			编辑专业: <img src="images/error.png" onclick="toggleMajorEdit(false);">
		</div>
		<form action="major/edit" method="post" onsubmit="return editMajor(this);">
			<!--提交记录id-->
			<input type="hidden" name="id" id="major_edit_id">
			<table>
				<tr>
					<td>专业名称:</td>
					<td><input type="text" name="major" id="major_edit_major">
					</td>
				</tr>
				<tr style="text-align: center;">
					<td colspan="2"><span class="error" id="major_edit_error">&nbsp;</span>
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
<script src="script/admin/major.js"></script>
<script src="script/time.js"></script>
<script src="script/tips.js"></script>
</html>