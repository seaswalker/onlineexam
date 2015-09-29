<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	pageContext.setAttribute("basePath", basePath);
%>
<html>
<head>
<title>考试记录</title>
<meta charset="UTF-8">
<base href="<%=basePath%>">
<link rel="SHORTCUT ICON" href="images/icon.ico">
<link rel="BOOKMARK" href="images/icon.ico">
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/head.css">
<link rel="stylesheet" type="text/css" href="css/student/exam_list.css">
</head>
<body>
	<!--头部-->
	<jsp:include page="share/head.jsp"></jsp:include>

	<!--中间主体部分-->
	<div class="main">
		<table class="table table-bordered">
			<caption>您的考试记录:</caption>
			<thead>
				<tr>
					<th>id</th>
					<th>试题名称</th>
					<th>考试分数</th>
					<th>考试时间</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${pageBean.records}" var="note">
					<tr>
						<td>${note.id}</td>
						<td>
							<a href="student/notes/view/${note.id}">${note.examTitle}</a>
						</td>
						<td>${note.point}</td>
						<td>
							<fmt:formatDate value="${note.time}" pattern="yyyy-MM-dd"/>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<!--分页-->
		<div class="page">
			<script type="text/javascript">
				function page(pageCode) {
					window.location.href = "${pageContext.request.contextPath}/student/notes/" + pageCode;
				}
			</script>
			<jsp:include page="../share/page.jsp"></jsp:include>
		</div>
	</div>
</body>
<script type="text/javascript" src="script/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<script src="script/time.js"></script>
</html>