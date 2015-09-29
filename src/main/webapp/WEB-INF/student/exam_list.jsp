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
<title>试卷</title>
<meta charset="UTF-8">
<base href="<%=basePath%>">
<link rel="SHORTCUT ICON" href="images/icon.ico">
<link rel="BOOKMARK" href="images/icon.ico">
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/head.css">
<link rel="stylesheet" type="text/css" href="css/student/exam_list.css">
<script type="text/javascript" src="script/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<script src="script/time.js"></script>
</head>
<body>
	<!--头部-->
	<jsp:include page="share/head.jsp"></jsp:include>

	<!--中间主体部分-->
	<div class="main">
		<table class="table table-bordered">
			<caption>您的考试:</caption>
			<thead>
				<tr>
					<th>id</th>
					<th>名称</th>
					<th>状态</th>
					<th>结束时间</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${pageBean.records}" var="exam">
					<tr>
						<td>${exam.id}</td>
						<c:choose>
							<c:when test="${exam.status == 'NOTRUN'}">
								<td>
									<a href="javascript:alert('考试尚未开始');">${exam.title}</a>
								</td>
								<td>尚未开始</td>
								<td>-</td>
							</c:when>
							<c:when test="${exam.status == 'RUNNING'}">
								<td>
									<a href="student/exam/${exam.id}">${exam.title}</a>
								</td>
								<td class="running">正在运行</td>
								<td>
									<fmt:formatDate value="${exam.endTime}" pattern="yyyy-MM-dd"/>
								</td>
							</c:when>
							<c:otherwise>
								<td>
									<a href="javascript:alert('考试已经结束');">${exam.title}</a>
								</td>
								<td class="runned">已经结束</td>
								<td>
									<fmt:formatDate value="${exam.endTime}" pattern="yyyy-MM-dd"/>
								</td>
							</c:otherwise>
						</c:choose>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<!--分页-->
		<div class="page">
			<script type="text/javascript">
				function page(pageCode) {
					window.location.href = "${pageContext.request.contextPath}/student/exam/list/" + pageCode;
				}
			</script>
			<jsp:include page="../share/page.jsp"></jsp:include>
		</div>
		
	</div>
</body>
</html>