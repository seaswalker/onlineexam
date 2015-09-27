<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="head">
	<div class="head_logo">
		<img src="images/logo.png"> <span class="logo_content">欢迎使用考试系统</span>
	</div>
	<!--显示登录的用户-->
	<div class="head_info">
		<div style="height: 80px;"></div>
		<div style="float: right;margin-right: 20px;">
			<span>欢迎:</span> <span style="color: red;">${sessionScope.teacher.name}</span>
			&nbsp;&nbsp; <span id="cur_time"></span>
			<c:if test="${!sessionScope.teacher.modified}">
				<!-- 如果是初始密码，提示修改密码 -->
				<span style="color: red;">您当前的密码是初始密码，请尽快修改</span>
			</c:if>
		</div>
	</div>
	<!--bootstrap导航条-->
	<nav class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="teacher/index">主页</a>
			</div>

			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> 试卷 <b class="caret"></b>
					</a>
						<ul class="dropdown-menu">
							<li><a href="teacher/exam/list">列表</a></li>
							<li><a href="teacher/exam/add">添加</a></li>
						</ul></li>
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> 题库 <b class="caret"></b>
					</a>
						<ul class="dropdown-menu">
							<li><a href="teacher/question/singles">单选题</a></li>
							<li><a href="teacher/question/multis">多选题</a></li>
							<li><a href="teacher/question/judges">判断题</a></li>
						</ul></li>
					<li><a href="teacher/password">修改密码</a></li>
					<li><a href="teacher/logout">退出系统</a></li>
				</ul>
			</div>
		</div>
	</nav>
</div>