//试卷列表操作
$(function() {
    ExamList.initListeners();
});

var ExamList = {
	//已选的班级的id, 实现已选的班级不再出现在下拉列表
	selectedClazzs: [],
	$gradeOptions: null,
	//缓存当前正在操作的试卷的id
	examId: 0,
	//校验正整数
	numberCheckPattern: /^[1-9][0-9]*$/,
	//辅助函数，获取当前行的试卷的id
	_getId: function(element) {
		return $(element).parents("tr").children("td:first")[0].innerHTML;
	},
	//绑定exam_list.jsp中的事件监听函数
	initListeners: function() {
		var that = this;
		//显示所选班级
		$("#exam-list button[name=show-clazz-btn]").click(function() {
			that.showClass(this);
		});
		//移除所选班级
		$("#remove-clazz-btn").click(that.removeClass);
		//关闭适用班级窗体
		$("#close-clazz-btn").click(that.closeClass);
		//删除试卷监听器
		$("#exam-list button[name=delete-exam-btn]").click(function(event) {
			that.removeExam(event.currentTarget);
		});
		//监听年级和专业的改变事件
		$("#grade_select").change(function() {
			that.loadMajor(this);
		});
		$("#major_select").change(function() {
			that.loadClazz(this);
		});
		//班级添加按钮
		$("#clazz-add-btn").click(that.addClass);
		//班级保存
		$("#clazz-save-btn").click(that.saveClass);
		//开始运行按钮
		$("#exam-list button[name=show-run-time-btn]").click(function() {
			that.showRunTime(this);
		});
		//关闭运行时间设置按钮
		$("#close-run-time-btn").click(that.closeRunTime);
		//保存运行时间
		$("#run-time-save-btn").click(that.saveRunTime);
		//立即停止
		$("#exam-list button[name=stop-run-btn]").click(function() {
			that.stopRun(this);
		});
		//显示试卷编辑界面
		$("#exam-list button[name=show-edit-btn]").click(function() {
			ExamList.showExamEdit(this);
		});
		//关闭试卷编辑界面
		$("#close-edit-btn").click(ExamList.closeExamEdit);
		//试卷保存
		$("#exam-save-btn").click(ExamList.saveExam);
	},
	//显示适用的班级
	//button 触发事件的dom对象
	showClass: function(button) {
		var $clazzShow = $("#clazz-show");
		//加载适用的班级
		var id = $(button).parent().prev().prev().html();
		ExamList.examId = id;
		$clazzShow.show();
		$.post("teacher/clazz/list", "examId=" + id, function(data) {
			if (data.result === "0") {
				Tips.showError(result.message);
			} else if (data.result === "1") {
				//渲染班级到clazz-show
				var classesStr = "";
				$.each(data.data, function(index, element) {
					ExamList.selectedClazzs.push(element.id);
					classesStr += "<li><input type='checkbox' /><span value='" + element.id + "'>" + element.grade.grade + "级" +
							element.major.name + element.cno + "班</span></li>";
				});
				$(classesStr).appendTo($("#exam-list-container").empty());
			}
		}, "json");
		$.post("grade/ajax", null, function(data) {
			if (data.result === "0") {
				Tips.showError("年级加载失败，请稍后再试");
			} else if (data.result === "1") {
				var optionsStr = "<option value='0'>年级...</option>";
				$.each(data.data, function(index, element) {
					optionsStr += "<option value='" + element.id + "'>" + element.grade + "级</option>";
				});
				ExamList.$gradeOptions = $(optionsStr);	
				ExamList.$gradeOptions.appendTo($("#grade_select").empty());
			}
		}, "json");
	},
	//关闭班级
	closeClass: function() {
		$("#clazz-show").hide();
		ExamList.resetSelect();
	},
	//保存班级
	saveClass: function() {
		$.post("teacher/clazz/reset", "examId=" + ExamList.examId + "&clazzIds=" + ExamList.selectedClazzs.join(), function(data) {
			if (data.result === "0") {
				Tips.showError("参数非法!");
			} else if (data.result === "1") {
				Tips.showSuccess("保存成功!");
			}
			ExamList.closeClass();
		}, "json");
	},
	//移除选定的试卷
	//button dom对象，触发事件的按钮
	removeExam: function(button) {
		if (confirm("您确定删除此试题以及所有的成绩记录?")) {
			var id = ExamList._getId(button);
			$.post("teacher/exam/remove", "examId=" + id, function(data) {
				if (data.result === "0") {
					Tips.showError("参数错误!");
				} else if (data.result === "1") {
					Tips.showSuccess("删除成功");
					setTimeout(function() {
						window.location.reload();
					}, 3000);
				}
			}, "json");
		}
	},
	//当年级改变时，改变(加载)专业列表
	//gradeSelect 年级下拉列表dom对象
	loadMajor: function(gradeSelect) {
		var selectedGradeId = $(gradeSelect).val();
		var $majorSelect = $("#major_select");
		if (selectedGradeId === "0") {
			//没有选中任何"有意义"的元素，那么清空专业列表(除了提示元素)
			$majorSelect.children("option:gt(0)").remove();
		} else {
			$.post("major/ajax", "grade=" + selectedGradeId, function(data) {
				if (data.result === "0") {
					Tips.showError("专业加载失败，请稍后再试");
				} else if (data.result === "1") {
					$majorSelect.empty();
					var optionsStr = "<option value='0'>专业...</option>";
					$.each(data.data, function(index, element) {
						optionsStr += "<option value='" + element.id + "'>" + element.name + "</option>";
					});
					$(optionsStr).appendTo($majorSelect);
				}
			}, "json");
		}
	},
	//同上，改变班级列表
	loadClazz: function(majorSelect) {
		var $clazzSelect = $("#clazz_select");
		var selectedMajorId = $(majorSelect).val();
		var selectedGradeId = $("#grade_select").val();
		if (selectedMajorId === "0") {
			$clazzSelect.children("option:gt(0)").remove();
		} else {
			$.post("clazz/ajax", "grade=" + selectedGradeId + "&major=" + selectedMajorId, function(data) {
				if (data.result === "0") {
					Tips.showError("班级加载失败，请稍候重试");
				} else if (data.result === "1") {
					$clazzSelect.empty();
					var optionsStr = "<option value='0'>班级...</option>";
					$.each(data.data, function(index, element) {
						if ($.inArray(element.id, ExamList.selectedClazzs) === -1) {
							optionsStr += "<option value='" + element.id + "'>" + element.cno + "班</option>";
						}
					});				
					$(optionsStr).appendTo($clazzSelect);
				}
			}, "json");
		}
	},
	//添加班级
	addClass: function() {
		var $clazzSelect = $("#clazz_select"),
			$majorSelect = $("#major_select"),
			$gradeSelect = $("#grade_select");
		//需要的数据
		var selectedClazzId, selectedClazzCno, selectedMajorName, selectedGradeGrade;
		if (check()) {
			ExamList.selectedClazzs.push(selectedClazzId);
			var optionStr = "<li><input type='checkbox'><span value='" + selectedClazzId + "'>" + selectedGradeGrade + selectedMajorName + selectedClazzCno +
				"</span></li>";
			$("#exam-list-container").append(optionStr);
			ExamList.resetSelect($gradeSelect, $majorSelect, $clazzSelect);
		}

		//校验，如果成功，那么为上面的数据赋值
		function check() {
			var gOptions = $gradeSelect.children("option:gt(0):selected"),
				$error = $("#clazz_error");
			if (gOptions.length === 0) {
				$error.html("请选择年级");
				return false;
			} else {
				selectedGradeGrade = gOptions[0].innerHTML;
			}
			var mOptions = $majorSelect.children("option:gt(0):selected");
			if (mOptions.length === 0) {
				$error.html("请选择专业");
				return false;
			} else {
				selectedMajorName = mOptions[0].innerHTML;
			}
			var cOptions = $clazzSelect.children("option:gt(0):selected");
			if (cOptions.length === 0) {
				$error.html("请选择班级");
				return false;
			} else {
				var option = cOptions[0];
				selectedClazzCno = option.innerHTML;
				selectedClazzId = option.value;
			}
			$error.html("");
			return true;
		}
	},
	//移除所选班级
	removeClass: function() {
		var $checkeds = $("#exam-list-container input:checked");
		if ($checkeds.length === 0) {
			$("#clazz_error").html("请选择要删除的班级");
		} else {
			var id, $this;
			$checkeds.each(function() {
				$this = $(this);
				id = $this.next().attr("value");
				//从已选班级id数组中移除
				ExamList.selectedClazzs.splice($.inArray(id, ExamList.selectedClazzs), 1);
				$this.parent().remove();
			});
			ExamList.resetSelect();
		}
	},
	//重置三个下拉列表
	resetSelect: function($gradeSelect, $majorSelect, $clazzSelect) {
		var $gs = $gradeSelect || $("#grade_select"),
			$ms = $majorSelect || $("#major_select"),
			$cs = $clazzSelect || $("#clazz_select");
		$gs.empty().append(ExamList.$gradeOptions.clone());
		$ms.empty().append("<option value='0'>专业...</option>");
		$cs.empty().append("<option value='0'>班级...</option>");
	},
	//显示运行时间设置界面
	showRunTime: function(button) {
		//保存正在操作的试卷的id
		ExamList.examId = ExamList._getId(button);
		$("#run-time-set").show();
	},
	//关闭运行时间设置按钮
	closeRunTime: function() {
		//清空输入框的内容的错误提示信息
		var $runTimeSet = $("#run-time-set");
		$runTimeSet.find("input").val("");
		$("#run-time-error").html("");
		$runTimeSet.hide();
	},
	//保存运行天数
	saveRunTime: function() {
		var $runTimeSet = $("#run-time-set");
		var $input = $runTimeSet.find("input");
		var days = $input.val();
		var $error = $("#run-time-error");
		if ($.trim(days) === "") {
			$error.html("请输入天数");
			$input.focus();
			return;
		} else if (!days.match(ExamList.numberCheckPattern)) {
			$error.html("请输入正整数");
			$input.focus();
			return;
		}
		//同步提交
		ExamList._sendStatusRequest(ExamList.examId, "RUNNING", days);
	},
	//发送切换试卷状态请求
	_sendStatusRequest: function(id, status, days) {
		//同步提交
		$.ajax({
			url: "teacher/exam/status",
			data: "examId=" + id + "&status=" + status + "&days=" + (days || ""),
			dataType: "json",
			async: false,
			success: function(data) {
				if (data.result === "0") {
					Tips.showError("参数错误!");
				} else if (data.result === "1") {
					Tips.showSuccess("保存成功");
					setTimeout(function() {
						window.location.reload();
					}, 3000);
				}
			} 
		});
	},
	//停止
	stopRun: function(button) {
		var id = ExamList._getId(button);
		//同步提交
		ExamList._sendStatusRequest(id, "RUNNED");
	},
	//显示试卷编辑界面
	//button 触发此事件的按钮
	showExamEdit: function(button) {
		var $examEdit = $("#exam-edit"), $inputs = $examEdit.find("input");
		var $tr = $(button).parents("tr"), $tds = $tr.children("td:lt(2)");
		ExamList.examId = $tds[0].innerHTML;
		//设置试卷题目
		$inputs[0].value = $tds[1].innerHTML;
		//设置时间限制
		$inputs[1].value = $tr.find("input[name=limit]").val();
		$examEdit.show();
	},
	//关闭试卷编辑界面
	closeExamEdit: function() {
		$("#exam-edit").hide();
	},
	//保存试卷
	saveExam: function() {
		//直接在这里校验
		var $title = $("#title-value");
		var title = $.trim($title.val());
		if (title === "") {
			getErrorSpan($title).html("请输入内容");
			$title.focus();
			return;
		}
		var $limit = $("#limit-value");
		var limit = $.trim($limit.val());
		if (limit === "") {
			getErrorSpan($limit).html("请输入内容");
			$limit.focus();
			return;
		}
		if (!limit.match(ExamList.numberCheckPattern)) {
			getErrorSpan($limit).html("格式非法");
			$limit.focus();
			return;
		}
		//提交请求
		$.post("teacher/exam/update/" + ExamList.examId, "title=" + title + "&limit=" + limit, function(data) {
			if (data.result === "0") {
				Tips.showError("更新失败，请稍候再试");
			} else if (data.result === "1") {
				Tips.showSuccess("更新成功");
				setTimeout(function() {
					window.location.reload();
				}, 2000);
			}
		}, "json");
		function getErrorSpan($ele) {
			return $ele.parent().next().children("span");
		}
	}
};