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
<title>试卷添加</title>
<meta charset="UTF-8">
<base href="<%=basePath%>">
<link rel="SHORTCUT ICON" href="images/icon.ico">
<link rel="BOOKMARK" href="images/icon.ico">
<link rel="stylesheet" type="text/css" href="bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="css/head.css">
<link rel="stylesheet" type="text/css" href="css/exam.css">
<link rel="stylesheet" type="text/css" href="css/modal.css">
<script type="text/javascript" src="script/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<script src="script/teacher/exam.js"></script>
<script src="script/tips.js"></script>
</head>
<body>
	<!--头部-->
	<jsp:include page="share/head.jsp"></jsp:include>
	
	<!--中间主体部分-->
	<div class="main">
		<input type="hidden" id="base-path" value="<%=basePath%>" />
		<!--单选题-->
		<div>
			<!--题目题型-->
			<div class="title">
				一、单选题
				<!--单选题总分数-->
				<span class="single_points">
				</span>
			</div>
			<!--题目-->
			<div id="single_questions">
				<div class="single_question">
					<!-- 记录此题的id -->
            		<input type="hidden" name="question-id" />
					<table class="question_table">
						<tr>
							<td name="single_index" width="5%">1</td>
							<td width="5%">、</td>
							<td width="20%">题目:</td>
							<td width="40%"><input type="text" name="single_title" class="required"></td>
							<td width="30%">
								<span class="error"></span>
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>选项A:</td>
							<td><input type="text" name="single_optionA" class="required"></td>
							<td>
								<span class="error"></span>
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>选项B:</td>
							<td><input type="text" name="single_optionB" class="required"></td>
							<td>
								<span class="error"></span>
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>选项C:</td>
							<td><input type="text" name="single_optionC" class="required"></td>
							<td>
								<span class="error"></span>
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>选项D:</td>
							<td><input type="text" name="single_optionD" class="required"></td>
							<td>
								<span class="error"></span>
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>答案:</td>
							<td name="single-options-container">
								<input type="radio" value="0" name="single-answer-1" checked="checked">A&nbsp;&nbsp;
								<input type="radio" value="1" name="single-answer-1">B&nbsp;&nbsp;
								<input type="radio" value="2" name="single-answer-1">C&nbsp;&nbsp;
								<input type="radio" value="3" name="single-answer-1">D
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>分值:</td>
							<td>
								<input type="text" style="width:100px;" name="single_point" class="number">
								<td>
									<span class="error"></span>
								</td>
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td colspan="2">
								<button name="remove-single-btn">&nbsp;移除此题</button>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<hr>
			<!--操作按钮-->
			<div class="btns">
				<button id="add-single-btn">添加问题</button>
				<button name="show-bank-btn" questionType="1">题库...</button>
			</div>
		</div>
		<!--多选题-->
		<div>
			<div class="title">
				二、多选题:
			</div>
			<div id="multi_questions">
				<div class="multi_question">
					<!-- 记录此题的id -->
            		<input type="hidden" name="question-id" />
					<table class="question_table">
						<tr>
							<td name="multi_index" width="5%">1</td>
							<td width="5%">、</td>
							<td width="20%">题目:</td>
							<td width="40%"><input type="text" name="multi_title" class="required"></td>
							<td width="30%">
								<span class="error"></span>
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>选项A:</td>
							<td><input type="text" name="multi_optionA" class="required"></td>
							<td>
								<span class="error"></span>
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>选项B:</td>
							<td><input type="text" name="multi_optionB" class="required"></td>
							<td>
								<span class="error"></span>
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>选项C:</td>
							<td><input type="text" name="multi_optionC" class="required"></td>
							<td>
								<span class="error"></span>
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>选项D:</td>
							<td><input type="text" name="multi_optionD" class="required"></td>
							<td>
								<span class="error"></span>
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>答案:</td>
							<td name="multi-options-container">
								<input type="checkbox" value="0">A&nbsp;&nbsp;
	                            <input type="checkbox" value="1">B&nbsp;&nbsp;
	                            <input type="checkbox" value="2">C&nbsp;&nbsp;
	                            <input type="checkbox" value="3">D
							</td>
							<td><span class="error"></span></td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>分值:</td>
							<td>
								<input type="text" style="width:100px;" name="multi_point" class="number">
							</td>
							<td><span class="error"></span></td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td colspan="2">
								<button name="remove-multi-btn">&nbsp;移除此题</button>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<hr>
			<!--操作按钮-->
			<div class="btns">
				<button id="add-multi-btn">添加问题</button>
				<button name="show-bank-btn" questionType="2">题库...</button>
			</div>
		</div>
		<!--判断题-->
		<div>
			<div class="title">
				三、判断题
			</div>
			<div id="judge_questions">
				<div class="judge_question">
					<!-- 记录此题的id -->
            		<input type="hidden" name="question-id" />
					<table class="judge_question_table">
						<tr>
							<td name="multi_index" width="5%">1</td>
							<td width="5%">、</td>
							<td width="20%">题目:</td>
							<td width="40%"><input type="text" name="judge_title" class="required"></td>
							<td width="30%">
								<span class="error"></span>
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>答案:</td>
							<td>
								<input type="radio" value="0" name="judge-answer-1" checked="checked">对&nbsp;&nbsp;
								<input type="radio" value="1" name="judge-answer-1">错
							</td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td>分值:</td>
							<td>
								<input type="text" style="width:100px;" name="judge_point" class="number">
							</td>
							<td><span class="error"></span></td>
						</tr>
						<tr>
							<td colspan="2"></td>
							<td colspan="2">
								<button name="remove-judge-btn">&nbsp;移除此题</button>
							</td>
						</tr>
					</table>
				</div>
			</div>
			<!--操作按钮-->
			<div class="btns">
				<button id="add-judge-btn">添加问题</button>
				<button name="show-bank-btn" questionType="3">题库...</button>
			</div>
		</div>
	</div>

	<!--底部-->
	<div class="bottom">
		<hr>
		<!--其它设置-->
		<div id="other-setting">
			<div class="title">选项设置:</div>
			<table class="setting_table">
				<tr>
					<td>试卷题目:</td>
					<td>
						<input class="required" type="text" name="exam_title"></input>
					</td>
					<td><span class="error"></span></td>
				</tr>
				<tr>
					<td>时间限制:</td>
					<td>
						<input type="text" class="number" name="time_limit">分钟
					</td>
					<td>
                        <span class="error"></span>
                    </td>
				</tr>
				<tr>
					<td>适用班级:</td>
					<td>
						<select id="grade_select">
							<option value="0">年级...</option>
						</select>
						<select id="major_select">
							<option value="0">专业...</option>
						</select>
						<select id="clazz_select">
							<option value="0">班级</option>
						</select>
					</td>
                    <td>
                        <span class="error"></span>
                    </td>
				</tr>
				<tr>
					<td>运行状态:</td>
					<td>
						<input type="radio" name="status" value="0" checked>暂不运行
						<input type="radio" name="status" value="1">保存后立即运行
					</td>
				</tr>
				<tr style="display:none;" id="run-time">
					<td>运行时长:</td>
					<td>
						<input type="text" class="number" name="run_time">天
					</td>
					<td><span class="error"></span></td>
				</tr>
			</table>
		</div>
		<!--按钮-->
		<div class=btns>
		    <button class="btn btn-success" id="save-btn">保存</button>
		    &nbsp;&nbsp;
		    <button class="btn btn-info" onclick="window.history.go(-1);">返回</button>
		</div>
	</div>
	
	<!--题型模版-->
	<div style="display:none;">
        <div id="single_question_model">
            <div class="single_question">
            	<!-- 记录此题的id -->
            	<input type="hidden" name="question-id" />
                <table class="question_table">
                    <tr>
                        <td name="single_index" width="5%">1</td>
                        <td width="5%">、</td>
                        <td width="20%">题目:</td>
                        <td width="40%"><input type="text" name="single_title" class="required"></td>
                        <td width="30%"><span class="error"></span></td>
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>选项A:</td>
                        <td><input type="text" name="single_optionA" class="required"></td>
                        <td><span class="error"></span></td>
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>选项B:</td>
                        <td><input type="text" name="single_optionB" class="required"></td>
                        <td><span class="error"></span></td>
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>选项C:</td>
                        <td><input type="text" name="single_optionC" class="required"></td>
                        <td><span class="error"></span></td>
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>选项D:</td>
                        <td><input type="text" name="single_optionD" class="required"></td>
                        <td><span class="error"></span></td>
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>答案:</td>
                        <td name="single-options-container">
                            <input type="radio" value="0" checked="checked">A&nbsp;&nbsp;
                            <input type="radio" value="1">B&nbsp;&nbsp;
                            <input type="radio" value="2">C&nbsp;&nbsp;
                            <input type="radio" value="3">D
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>分值:</td>
                        <td>
                            <input type="text" style="width:100px;" name="single_point" class="number">
                        </td>
                        <td><span class="error"></span></td>
                    </tr>
                    <tr>
                    	<td colspan="2"></td>
                    	<td colspan="2">
                            <button name="remove-single-btn">&nbsp;移除此题</button>
                    	</td>
                    </tr>
                </table>
            </div>
         </div>
        <div id="multi_question_model">
            <div class="multi_question">
            	<!-- 记录此题的id -->
            	<input type="hidden" name="question-id" />
                <table class="question_table">
                   <tr>
						<td name="multi_index" width="5%">1</td>
						<td width="5%">、</td>
						<td width="20%">题目:</td>
						<td width="40%"><input type="text" name="multi_title" class="required"></td>
						<td width="30%">
							<span class="error"></span>
						</td>
					</tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>选项A:</td>
                        <td><input type="text" name="multi_optionA" class="required"></td>
						<td><span class="error"></span></td>						
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>选项B:</td>
                        <td><input type="text" name="multi_optionB" class="required"></td>
						<td><span class="error"></span></td>						
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>选项C:</td>
                        <td><input type="text" name="multi_optionC" class="required"></td>
						<td><span class="error"></span></td>						
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>选项D:</td>
                        <td><input type="text" name="multi_optionD" class="required"></td>
						<td><span class="error"></span></td>
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>答案:</td>
                        <td name="multi-options-container">
                            <input type="checkbox" value="0">A&nbsp;&nbsp;
                            <input type="checkbox" value="1">B&nbsp;&nbsp;
                            <input type="checkbox" value="2">C&nbsp;&nbsp;
                            <input type="checkbox" value="3">D
                        </td>
                        <td><span class="error"></span></td>
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>分值:</td>
                        <td>
                            <input type="text" style="width:100px;" name="multi_point" class="number">
                        </td>
                        <td><span class="error"></span></td>
                    </tr>
                    <tr>
                    	<td colspan="2"></td>
                    	<td colspan="2">
                            <button name="remove-multi-btn">&nbsp;移除此题</button>
                    	</td>
                    </tr>
                </table>
            </div>
        </div>
        <div id="judge_question_model">
            <div class="judge_question">
            	<!-- 记录此题的id -->
            	<input type="hidden" name="question-id" />
                <table class="judge_question_table">
                    <tr>
						<td name="judge_index" width="5%">1</td>
						<td width="5%">、</td>
						<td width="20%">题目:</td>
						<td width="40%"><input type="text" name="judge_title" class="required"></td>
						<td width="30%">
							<span class="error"></span>
						</td>
						</tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>答案:</td>
                        <td>
                            <input type="radio" value="0" checked="checked">对&nbsp;&nbsp;
                            <input type="radio" value="1">错
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2"></td>
                        <td>分值:</td>
                        <td>
                            <input type="text" style="width:100px;" name="judge_point" class="number">
                        </td>
                        <td><span class="error"></span></td>	
                    </tr>
                    <tr>
						<td colspan="2"></td>
						<td colspan="2">
							<button name="remove-judge-btn">&nbsp;移除此题</button>
						</td>
					</tr>
                </table>
            </div>
        </div>
    </div>
    
    <!--题库-->
    <div class="modal_window" id="question_bank">
        <div class="modal_window_title">
        	选择题目: <img src="images/error.png" alt="关闭" id="hide-bank">
        </div>
        <div class="questions_area">
        	<ul id="bank-container"></ul>
        </div>
        <div class="btns">
        	<button id="add-question-bank-btn">添加</button>
        </div>
    </div>
</body>
</html>