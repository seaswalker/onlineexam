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
<title>修改密码</title>
<meta charset="UTF-8">
<base href="<%=basePath%>">
<link rel="SHORTCUT ICON" href="images/icon.ico">
<link rel="BOOKMARK" href="images/icon.ico">
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/head.css">
<link rel="stylesheet" type="text/css" href="css/list_main.css">
<style type="text/css">
	.password-form {
		margin: 0px auto 0px;
		width: 45%;
		height: 200px;
	}
	.password-error {
		font-size: 14px;
		color: red;
		display: inline-block;
		margin-left: 20px;
	}
</style>
</head>
<body>
	<!--头部-->
	<jsp:include page="share/head.jsp"></jsp:include>

	<!--中间主体部分-->
	<div class="main">
		<form action="admin/password/modify" method="post" role="form" id="password-form">
			<table class="password-form">
				<tr>
					<td style="width: 30%;">请输入旧密码:</td>
					<td style="width: 40%;">
						<input type="password" name="oldPassword" class="form-control">
					</td>
					<td style="width: 35%;">
						<span class="password-error"></span>
					</td>
				</tr>
				<tr>
					<td>
						请输入新密码:<br>
						<span style="font-size: 12px;color: grey;">*4-10位字母数字下划线组成</span>
					</td>
					<td>
						<input type="password" name="newPassword" class="form-control">
					</td>
					<td>
						<span class="password-error"></span>
					</td>					
				</tr>
				<tr>
					<td>请重复新密码:</td>
					<td>
						<input type="password" name="newPasswordConfirm" class="form-control">
					</td>
					<td>
						<span class="password-error"></span>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center;">
						<input type="submit" value="保存" class="btn btn-info">
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
<script type="text/javascript" src="script/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<script src="script/time.js"></script>
<script type="text/javascript">
	$(function() {
		var $form = $("#password-form"),
			form = $form[0],
			oldPassword = form.oldPassword,
			newPassword = form.newPassword,
			newPasswordConfirm = form.newPasswordConfirm;
		var $errors = $form.find("span.password-error");
		var opError = $errors[0],
			npError = $errors[1],
			npcError = $errors[2];
		//规定密码由4-10位的字母数字下划线组成
		var passwordPattern = /^\w{4,10}$/;
		$(oldPassword).blur(checkOldPassword).focus(function() {
			this.value = "";
			opError.innerHTML = "";
		});
		$(newPassword).blur(checkPassword).focus(function() {
			this.value = "";
			npError.innerHTML = "";
		});
		$(newPasswordConfirm).blur(checkConfirm).focus(function() {
			this.value = "";
			npcError.innerHTML = "";
		});
		$form.submit(function() {
			return checkOldPassword() && checkPassword() && checkConfirm();
		});

		function checkOldPassword() {
			var value = $.trim(oldPassword.value);
			if (value === "") {
				opError.innerHTML = "请输入旧密码";
				return false;
			}
			//发送ajax请求校验
			var flag = true;
			$.ajax({
				url: "admin/password/check",
				dataType: "json",
				data: "password=" + value,
				async: false,
				success: function(data) {
					if (data.result === "0") {
						opError.innerHTML = "您的密码错误";
						flag = false;
					}
				}
			});
			return flag;
		}

		function checkPassword() {
			var value = $.trim(newPassword.value);
			if (value === "") {
				npError.innerHTML = "请输入您的新密码";
				return false;
			} 
			if (!value.match(passwordPattern)) {
				npError.innerHTML = "您的密码格式非法";
				return false;
			}
			return true;
		}

		function checkConfirm() {
			if (newPassword.value !== newPasswordConfirm.value) {
				npcError.innerHTML = "您的两此新密码不一致";
				return false;
			}
			return true;
		}
	});
</script>
</html>