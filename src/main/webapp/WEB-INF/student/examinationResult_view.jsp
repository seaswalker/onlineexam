<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
<title>考试结果</title>
<meta charset="UTF-8">
<base href="<%=basePath%>">
<link rel="SHORTCUT ICON" href="images/icon.ico">
<link rel="BOOKMARK" href="images/icon.ico">
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/head.css">
<link rel="stylesheet" type="text/css" href="css/student/exam_take.css">
</head>
<body>
	<!--头部-->
	<jsp:include page="share/head.jsp"></jsp:include>

	<!--中间主体部分-->
	<div class="main">
		<div style="text-align: center;">
			您的分数: <span style="color: red;">${view.point}</span>
			&nbsp;&nbsp;考试时间: <span style="color: red;"><fmt:formatDate value="${view.time}" pattern="yyyy年MM月dd日"/></span>
		</div>
		<hr />
		<div id="single-container">
			<!-- 题型标识 -->
			<div class="title">
				一、单选题　(共${view.singlePoints}分)
			</div>
			<c:forEach items="${view.singleQuestions}" var="question" varStatus="status">
				<div class="question">
					<div class="question-title">
						${status.index + 1}.&nbsp;&nbsp;${question.title} (${question.point}分)
						<!-- 显示回答错误或是正确 -->
						<c:choose>
							<c:when test="${question.right}">
								<span class="right">回答正确</span>
							</c:when>
							<c:otherwise>
								<span class="error">回答错误</span>
							</c:otherwise>
						</c:choose>
					</div>
					<ul class="question-option">
						<li>A.&nbsp;&nbsp;${question.optionA}</li>
						<li>B.&nbsp;&nbsp;${question.optionB}</li>
						<li>C.&nbsp;&nbsp;${question.optionC}</li>
						<li>D.&nbsp;&nbsp;${question.optionD}</li>
					</ul>
					<div class="question-answer">
						答案:
						<input type="radio" name="single-${status.index}" ${question.answer == '0' ? 'checked' : ''} />A
						<input type="radio" name="single-${status.index}" ${question.answer == '1' ? 'checked' : ''} />B
						<input type="radio" name="single-${status.index}" ${question.answer == '2' ? 'checked' : ''} />C
						<input type="radio" name="single-${status.index}" ${question.answer == '3' ? 'checked' : ''} />D
						<c:if test="${!question.right}">
							<span class="error">您的答案:${question.wrongAnswerFacade}</span>
						</c:if>
					</div>
				</div>
			</c:forEach>
		</div>
		<hr>
		<!-- 多选题 -->
		<div id="multi-container">
			<div class="title">
				二、多选题　(共${view.multiPoints}分)
			</div>
			<c:forEach items="${view.multiQuestions}" var="question" varStatus="status"> 
				<div class="question">
					<div class="question-title">
						${status.index + 1}.&nbsp;&nbsp;${question.title} (${question.point}分)
						<!-- 显示回答错误或是正确 -->
						<c:choose>
							<c:when test="${question.right}">
								<span class="right">回答正确</span>
							</c:when>
							<c:otherwise>
								<span class="error">回答错误</span>
							</c:otherwise>
						</c:choose>
					</div>
					<ul class="question-option">
						<li>A.&nbsp;&nbsp;${question.optionA}</li>
						<li>B.&nbsp;&nbsp;${question.optionB}</li>
						<li>C.&nbsp;&nbsp;${question.optionC}</li>
						<li>D.&nbsp;&nbsp;${question.optionD}</li>
					</ul>
					<div class="question-answer">
						答案:
						<input type="checkbox" value="0" ${fn:contains(question.answer, '0') ? 'checked' : ''} />A
						<input type="checkbox" value="1" ${fn:contains(question.answer, '1') ? 'checked' : ''} />B
						<input type="checkbox" value="2" ${fn:contains(question.answer, '2') ? 'checked' : ''} />C
						<input type="checkbox" value="3" ${fn:contains(question.answer, '3') ? 'checked' : ''} />D
						<c:if test="${!question.right}">
							<span class="error">您的答案:${question.wrongAnswerFacade}</span>
						</c:if>
					</div>
				</div>
			</c:forEach>
		</div>
		<hr>
		<!-- 判断题 -->
		<div id="judge-container">
			<div class="title">
				三、判断题　(共${view.judgePoints}分)
			</div>
			<c:forEach items="${view.judgeQuestions}" var="question" varStatus="status">
				<div class="question">
					<div class="question-title">
						${status.index + 1}.&nbsp;&nbsp;${question.title} (${question.point}分)
						<!-- 显示回答错误或是正确 -->
						<c:choose>
							<c:when test="${question.right}">
								<span class="right">回答正确</span>
							</c:when>
							<c:otherwise>
								<span class="error">回答错误</span>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="question-answer">
						答案:
						<input type="radio" name="judge-${status.index}" ${question.answer == '0' ? 'checked' : ''} />对
						<input type="radio" name="judge-${status.index}" ${question.answer == '1' ? 'checked' : ''} />错
						<c:if test="${!question.right}">
							<span class="error">您的答案:${question.wrongAnswerFacade}</span>
						</c:if>
					</div>
				</div>
			</c:forEach>
		</div>
		<hr />
		<div style="text-align: center;margin-bottom: 20px;">
			<button class="btn btn-default" onclick="javascript:window.history.go(-1);">返回</button>
		</div>
	</div>
</body>
<script type="text/javascript" src="script/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<script src="script/time.js"></script>
</html>