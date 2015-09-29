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
<title>学生管理</title>
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
		<!--学生-->
		<div class="list" id="student_list">
			<!--搜索框-->
			<div class="search form-inline">
				<form action="admin/student/list" method="post" onsubmit="return searchStudent(this);">
					<input type="text" class="form-control" name="search" style="width: 300px;">
					&nbsp;&nbsp;
					<button class="btn btn-default" type="submit">搜索</button>
				</form>
			</div>
			<!--操作按钮-->
			<div class="operation_btn">
				<button class="btn btn-success btn-xs" onclick="toggleStudentAdd(true);">添加学生</button>
			</div>
			<table class="table table-hover">
				<thead>
					<tr>
						<th width="15%">学号</th>
						<th width="30">学生姓名</th>
						<th width="40%">班级</th>
						<th width="15%">操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageBean.records}" var="student">
						<tr>
							<td>${student.id}</td>
							<td>${student.name}</td>
							<td>${student.clazz.grade.grade}级${student.clazz.major.name}${student.clazz.cno}班</td>
							<td>
								<button class="btn btn-default btn-xs" onclick="toggleStudentEdit(true, this);">编辑</button>
								<button class="btn btn-danger btn-xs" onclick="deleteStudent(this);">删除</button>
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
					window.location.href = "${pageContext.request.contextPath}/admin/student/list?pn=" + pageCode + "&search=" + search;
				}
			</script>
			<jsp:include page="../share/page.jsp"></jsp:include>
		</div>
	</div>
	
	<!--学生添加-->
	<div class="modal_window student_window form-control" id="student_add">
		<!--标题-->
		<div class="modal_window_title">
			添加学生: <img src="images/error.png" onclick="toggleStudentAdd(false);">
		</div>
		<form action="admin/student/add" method="post" onsubmit="return addStudent(this);">
			<table>
				<tr>
					<td>学号:</td>
					<td>
						<input type="text" name="id">
					</td>
				</tr>
				<tr>
					<td>
						年级:
					</td>
					<td>
						<select name="grade" id="grade_select_add" onchange="changeMajor(this, true);">
							<option value="0">年级...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						专业:
					</td>
					<td>
						<select id="major_select_add" name="major" onchange="changeClazz(this, true);">
							<option value="0">专业...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						班级:
					</td>
					<td>
						<select id="clazz_select_add" name="clazz">
							<option value="0">班级...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>学生姓名:</td>
					<td><input type="text" name="student"></td>
				</tr>
				<tr style="text-align: center;">
					<td colspan="2"><span class="error" id="student_add_error">&nbsp;</span>
					</td>
				</tr>
				<tr style="text-align: center;">
					<td colspan="2"><input type="submit" value="提交"></td>
				</tr>
			</table>
		</form>
	</div>
	<!--学生修改-->
	<div class="modal_window student_window form-control" id="student_edit">
		<!--标题-->
		<div class="modal_window_title">
			编辑学生: <img src="images/error.png" onclick="toggleStudentEdit(false);">
		</div>
		<form action="" id="student_edit_form" method="post" onsubmit="return editStudent(this);">
			<!--提交记录id-->
			<input type="hidden" name="id" id="student_edit_id">
			<table>
				<tr>
					<td>
						年级:
					</td>
					<td>
						<select name="grade" id="grade_select_edit" onchange="changeMajor(this, false);">
							<option value="0">年级...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						专业:
					</td>
					<td>
						<select id="major_select_edit" name="major" onchange="changeClazz(this, false);">
							<option value="0">专业...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						班级:
					</td>
					<td>
						<select id="clazz_select_edit" name="clazz">
							<option value="0">班级...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>学生姓名:</td>
					<td><input type="text" name="student" id="student_edit_name"></td>
				</tr>
				<tr style="text-align: center;">
					<td colspan="2"><span class="error" id="student_edit_error">&nbsp;</span>
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
<script src="script/admin/student.js"></script>
<script src="script/time.js"></script>
<script src="script/tips.js"></script>
</html>