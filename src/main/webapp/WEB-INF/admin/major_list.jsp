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
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/head.css">
<link rel="stylesheet" type="text/css" href="css/list_main.css">
<script type="text/javascript" src="script/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<script src="script/admin/major.js"></script>
<script src="script/time.js"></script>
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
				<button class="btn btn-danger btn-sm" onclick="deleteElements();">删除</button>
				<button class="btn btn-success btn-sm"
					onclick="toggleMajorAdd(true);">添加专业</button>
			</div>
			<table class="table table-hover">
				<thead>
					<tr>
						<th width="10%"><input type="checkbox"
							onchange="chooseAll(this);" id="checkAll"> <label
							for="checkAll">全选</label></th>
						<th width="15%">id</th>
						<th width="50%">专业名称</th>
						<th width="25%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageBean.records}" var="major">
						<tr>
							<td><input type="checkbox" name="cb"></td>
							<td>${major.id}</td>
							<td>${major.name}</td>
							<td>
								<button class="btn btn-default"
									onclick="toggleMajorEdit(true, this);">编辑</button>
								<button class="btn btn-danger" onclick="deleteElement(this);">删除</button>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<!--分页-->
		<div class="page">
			<ul class="pagination">
				<!-- 上一页 -->
				<c:choose>
					<c:when test="${pageBean.currentPage > 1}">
						<li><a href="admin/major/list?pn=${pageBean.currentPage - 1}&search=${search}">&laquo;</a></li>
					</c:when>
					<c:otherwise>
						<li class="disabled"><a href="#">&laquo;</a></li>
					</c:otherwise>
				</c:choose>
				<!-- 迭代页码 -->
				<c:forEach begin="${pageBean.pageBeginIndex}" end="${pageBean.pageEndIndex}" var="code">
					<c:choose>
						<c:when test="${pageBean.currentPage == code}">
							<li class="active"><a href="#">${code}</a></li>
						</c:when>
						<c:otherwise>
							<li><a href="admin/major/list?pn=${code}&search=${search}">${code}</a></li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<!-- 下一页 -->
				<c:choose>
					<c:when test="${pageBean.currentPage < pageBean.pageCount}">
						<li><a href="admin/major/list?pn=${pageBean.currentPage + 1}&search=${search}">&raquo;</a></li>
					</c:when>
					<c:otherwise>
						<li class="disabled"><a href="#">&raquo;</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
	
	<!--专业添加-->
	<div class="major_add form-control" id="major_add">
		<!--标题-->
		<div class="ma_title">
			添加专业: <img src="images/error.png" onclick="toggleMajorAdd(false);">
		</div>
		<form action="major/add" method="post"
			onsubmit="return checkMajor(this);">
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
	<div class="major_add form-control" id="major_edit">
		<!--标题-->
		<div class="ma_title">
			编辑专业: <img src="images/error.png" onclick="toggleMajorEdit(false);">
		</div>
		<form action="major/edit" method="post"
			onsubmit="return checkMajor(this);">
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
</html>