<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String path = request.getContextPath(); 
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
	pageContext.setAttribute("basePath",basePath); 
%> 
<html>
<head>
<base href="<%=basePath%>">
<title>用户登录</title>
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" href="css/login.css">
<link rel="SHORTCUT ICON" href="images/icon.ico">
<link rel="BOOKMARK" href="images/icon.ico">
<script src="script/jquery-1.11.1.min.js"></script>
<script src="script/login.js"></script>
</head>
<body>
	<!--顶部-->
	<div class="top">
		<img src="images/login/top.png">
	</div>
	<div class="middle">
		<div class="middle_deco">
			<table class="table">
				<tr>
					<td><img src="images/exam.png" class="login_exam"></td>
					<td class="login_title">&nbsp;&nbsp;Exam System&nbsp;&nbsp;</td>
				</tr>
			</table>
		</div>
		<!--中间登录-->
		<div class="middle_login">
			<form action="login/do" method="post" onsubmit="return check(this);">
				<table class="main_font login_table">
					<tr>
						<td>用户名:</td>
						<td><input type="text" name="username"></td>
					</tr>
					<tr>
						<td>密码:</td>
						<td><input type="password" name="password"></td>
					</tr>
					<tr>
						<td>验证码:</td>
						<td>
							<input type="text" name="verify" class="verify_input"><img src="image.jsp" onclick="image(this);" class="verify_image">
						</td>
					</tr>
					<tr>
						<td>登录角色:</td>
						<td><input id="radio_student" type="radio" name="role"
							value="1" checked> <label for="radio_student">学生</label>
							<input id="radio_teacher" type="radio" name="role" value="2">
							<label for="radio_teacher">教师</label>
							<input id="radio_manager" type="radio" name="role" value="3">
							<label for="radio_manager">管理员</label>
						</td>
					</tr>
					<tr>
						<td colspan="2"><span class="error" id="login_error">&nbsp;${error}</span>
						</td>
					</tr>
					<tr>
						<td colspan="2" style="text-align: center;"><input
							type="submit" value="登录"> <input type="reset" value="重置">
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<!--尾部-->
	<div class="bottom">
		<img src="images/login/bottom.png">
	</div>
</body>
</html>