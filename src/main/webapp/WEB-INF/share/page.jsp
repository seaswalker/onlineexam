<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<ul class="pagination">
	<!-- 上一页 -->
	<c:choose>
		<c:when test="${pageBean.currentPage > 1}">
			<li><a href="javascript:page(${pageBean.currentPage - 1});">&laquo;</a></li>
		</c:when>
		<c:otherwise>
			<li class="disabled"><a href="javascript:void(0);">&laquo;</a></li>
		</c:otherwise>
	</c:choose>
	<!-- 迭代页码 -->
	<c:forEach begin="${pageBean.pageBeginIndex}" end="${pageBean.pageEndIndex}" var="code">
		<c:choose>
			<c:when test="${pageBean.currentPage == code}">
				<li class="active"><a href="javascript:void(0);">${code}</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="javascript:page(${code});">${code}</a></li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	<!-- 下一页 -->
	<c:choose>
		<c:when test="${pageBean.currentPage < pageBean.pageCount}">
			<li><a href="javascript:page(${pageBean.currentPage + 1});">&raquo;</a></li>
		</c:when>
		<c:otherwise>
			<li class="disabled"><a href="javascript:void(0);">&raquo;</a></li>
		</c:otherwise>
	</c:choose>
</ul>