/**
 * [[删除单个元素]]
 * @param {[[DOM]]} btn [[触发此函数的按钮]]
 */
function deleteStudent(btn) {
	var id = $(btn).parents("tr").find("td:first").html();
	if(confirm("这会导致相应的班级及学生被删除,您确定?")) {
		sendDeleteRequest(id);
	}
}

/**
 * [[发送删除请求]]
 * @param {[[String]]} [[请求参数]]
 */
function sendDeleteRequest(id) {
	$.ajax({
		"url" : "admin/student/delete/" + id,
		"dataType" : "json",
		"async" : false,
		"success" : function(json) {
			if(json.result == 0) {
				Tips.showError(json.message);
			}else if(json.result == 1) {
				Tips.showSuccess(json.message);
				window.location.reload();
			}
		}
	});
}

/**
 * [[检查学生]]
 * @param {[[DOM]]} form [[所在表单]]
 * @param {[[DOM]]} error [[显示错误的DOM]]
 * @param {boolean} isAdd 是否是学生添加，如果不是，那么只需要检查姓名，因为年级、专业以及班级没有空这一选项，并且没有id字段
 */
function _checkStudent(form, error, isAdd) {
	if(isAdd) {
		var id = form.id;
		var id_value = id.value.trim();
		if (id_value == "") {
			error.innerHTML = "请输入学号";
			id.focus();
			return false;
		}
		//检查是否是数字
		var pattern = new RegExp("^[1-9][0-9]*$");
		if(!id_value.match(pattern)) {
			error.innerHTML = "格式有误，示例2012";
			id.focus();
			return false;
		}
	}
	//检测是否选择了年级
	var gradeSelectValue = isAdd ? $("#grade_select_add").val() : $("#grade_select_edit").val();
	if(gradeSelectValue == undefined || gradeSelectValue == "0") {
		error.innerHTML = "请选择年级";
		return false;
	}
	//检测专业
	var majorSelectValue = isAdd ? $("#major_select_add").val() : $("#major_select_edit").val();;
	if(majorSelectValue == undefined || majorSelectValue == "0") {
		error.innerHTML = "请选择专业";
		return false;
	}
	//检测班级
	var clazzSelectValue = isAdd ? $("#clazz_select_add").val() : $("#grade_select_edit").val();
	if(clazzSelectValue == undefined || clazzSelectValue == "0") {
		error.innerHTML = "请选择班级";
		return false;
	}
	//检测学生姓名
	var student = form.student;
	if(student.value.trim() == "") {
		error.innerHTML = "请输入学生姓名";
		student.focus();
		return false;
	}
	var flag = true;
	if(isAdd) {
		//检测学号是否存在
		$.ajax({
			"url": "admin/student/check",
			"data": "id=" + id_value,
			"async": false,
			"dataType": "json",
			"success": function(json) {
				if(json.result == "0") {
					error.innerHTML = json.message;
				}else if(json.result == "1") {
					if(json.exist == "1") {
						error.innerHTML = "此学号已存在";
						flag = false;
					}
				}
			}
		});
	}
	return flag;
}

/**
 * [[添加专业]]
 * @param {[[DOM]]} form [[所在的表单]]
 */
function addStudent(form) {
    var error = document.getElementById("student_add_error");
    if(_checkStudent(form, error, true)) {
        $.ajax({
            "url": "admin/student/add",
            "data": "id=" + form.id.value + "&clazz=" + $("#clazz_select_add").val() + "&name=" + form.student.value,
            "async": false,
            "dataType": "json",
            "success": function(json) {
                if(json.result == 0) {
                    error.innerHTML = json.message;
                }else {
                    toggleStudentAdd(false);
                    _resetStudent(form.student, error);
                    Tips.showSuccess(json.message);
                }
            }
        });
    }
    return false;
}

/**
 * 编辑学生
 * @param form
 */
function editStudent(form) {
    var error = document.getElementById("student_edit_error");
    if(_checkStudent(form, error, false)) {
        $.ajax({
            "url": "admin/student/edit",
            "data": "name=" + form.student.value + "&id=" + form.id.value + "&clazz=" + $("#clazz_select_edit").val(),
            "async": false,
            "dataType": "json",
            "success": function(json) {
                if(json.result == 0) {
                    error.innerHTML = json.message;
                }else {
                    toggleStudentEdit(false);
                    _resetStudent(form.student, error);
                    Tips.showSuccess(json.message);
                    window.location.href = "admin/student/list";
                }
            }
        });
    }
    return false;
}

/**
 * [[显示/隐藏专业添加窗口]
 */
function toggleStudentAdd(isShow) {
	var student_add = document.getElementById("student_add");
	if(isShow) {
		//加载所有年级
		_loadGrades(function(element) {
			return "<option value=" + element.id + ">" + element.grade + "</option>";
		}, true);
		student_add.style.display = "block";
	}else {
		student_add.style.display = "none";
	}
}

/**
 * 加载所有年级
 * @param callback 回调函数
 * 此回调函数接收可以接收两个参数，第一个是element(需要处理的json对象)
 * 此函数需要返回一个代表option元素的字符串
 * @param isAdd 是否是学生增加
 */
function _loadGrades(callback, isAdd) {
	$.ajax({
		"url": "grade/ajax",
		"dataType": "json",
		"async": false,
		"success": function(json) {
			if(json.result == "1") {
				var $gradeSelect = isAdd ? $("#grade_select_add") : $("#grade_select_edit");
				$gradeSelect.empty();
				var options = new Array();
				//只有在增加时才显示提示
				if(isAdd) {
					options.push("<option value='0'>年级...</option>");
				}
				for(var i = 0;i < json.data.length;i ++) {
					options.push(callback(json.data[i]));
				}
				$gradeSelect.append($(options.join("")));
			}
		}
	});
}

/**
 * 利用ajax把专业加载到select元素
 * @param {Number} grade 年级id
 * @param {Boolean} isAdd 是否是学生添加，如果是，那么需要加入一个空白元素
 * @param {Function} callback 回调函数，用以决定如何利用给定的json对象生成option元素
 * 此回调函数可以接收一个element参数(当前的json对象)
 */
function _loadMajor(grade, isAdd, callback) {
	var $majorSelect = isAdd ? $("#major_select_add") : $("#major_select_edit");
	$.ajax({
		"url": "major/ajax",
		"data": "grade=" + grade,
		"dataType": "json",
		"async": false,
		"success": function(json) {
			if(json.result == "0") {
				Tips.showError(json.message);
			}else if(json.result == "1") {
				$majorSelect.empty();
				//专业数组
				var options = new Array();
				if(isAdd) {
					options.push("<option value='0'>专业...</option>");
				}
				for(var i = 0;i < json.data.length;i ++) {
					options.push(callback(json.data[i]));
				}
				$majorSelect.append($(options.join("")));
			}
		}
	});
}

/**
 * 当年级改变时联动专业
 * @param {DOM} select 年级下拉列表
 * @param {boolean} isAdd 是否是学生添加，此参数用以实现代码重用
 */
function changeMajor(select, isAdd) {
	var grade = $(select).val();
	_loadMajor(grade, isAdd, function(element) {
		return "<option value='" + element.id + "'>" + element.name + "</option>";
	});
}

/**
 * 加载班级
 * @param  {[Number]}   majorId  [专业id]
 * @param  {Boolean}  isAdd    [是否是学生添加，如果是需要加上空白元素]
 * @param  {Function} callback [回调函数，可以接收一个代表当前json对象的参数]
 * @return {[无]}            [没有返回值]
 */
function _loadClazz(majorId, isAdd, callback) {
	var $gradeSelect, $clazzSelect;
	var options = new Array();
	if(isAdd) {
		$gradeSelect = $("#grade_select_add");
		$clazzSelect = $("#clazz_select_add");
		options.push("<option value='0'>班级...</option>");	
	}else {
		$gradeSelect = $("#grade_select_edit");
		$clazzSelect = $("#clazz_select_edit");
	}
	var grade = $gradeSelect.val();
	$.ajax({
		"url": "clazz/ajax",
		"data": "grade=" + grade + "&major=" + majorId,
		"async": false,
		"dataType": "json",
		"success": function(json) {
			if(json.result == "0") {
				Tips.showError(json.message);
			}else if(json.result == "1") {
				$clazzSelect.empty();
				for(var i = 0;i < json.data.length;i ++) {
					options.push(callback(json.data[i]));
				}
				$clazzSelect.append($(options.join("")));
			}
		}
	});
}

/**
 * 专业变化时，根据年级和专业查出班级
 * @param {Boolean} isAdd 是否是学生添加，用以实现代码复用
 */
function changeClazz(select, isAdd) {
	var majorId = $(select).val();
	_loadClazz(majorId, isAdd, function(element) {
		return "<option value='" + element.id + "'>" + element.cno + "班</option>";
	});
}

/**
 * [[重置专业输入界面]]
 * @param {[[DOM]]} student [[专业输入]]
 * @param {[[DOM]]} error [[错误显示]]
 */
function _resetStudent(student, error) {
    student.value = "";
    error.innerHTML = "";
}

/**
 * [[显示/隐藏专业编辑窗口]]
 * @param {[[boolean]} [[true--显示]]
 * @param {[[DOM]]} [[如果是显示，那么为触发的按钮]]
 */
function toggleStudentEdit(isShow, btn) {
	var student_edit = document.getElementById("student_edit");
	if (isShow) {
		//班级信息栏
		var clazz_td = $(btn).parent().prev();
		//示例2012级电子信息科学与技术2班
		var clazzInfo = clazz_td.text();
		//利用正则表达式获取年级、专业、班级信息
		var pattern = new RegExp("^([0-9]+)级(.+)([0-9]+).+");
		var matches = pattern.exec(clazzInfo);
		var grade = matches[1];
		var gradeId = 0;
		var majorId = 0;
		var major = matches[2];
		var clazz = matches[3];
		//加载年级，并且设置回显
		_loadGrades(function(element) {
			var option = "<option value='" + element.id + "'";
			if(element.grade == grade) {
				option += " selected";
				gradeId = element.id;
			}
			option += ">" + element.grade + "</option>";
			return option;
		}, false);
		//加载专业，并且设置回显
		_loadMajor(gradeId, false, function(element) {
			var option = "<option value='" + element.id + "'";
			if(element.name == major) {
				option += " selected";
				majorId = element.id;
			}
			option += ">" + element.name + "</option>";
			return option;
		});
		//加载班级，并且设置回显
		_loadClazz(majorId, false, function(element) {
			var option = "<option value='" + element.id + "'";
			if(element.cno == clazz) {
				option += " selected";
			}
			option += ">" + element.cno + "班</option>";
			return option;
		});
		var name_td = clazz_td.prev();
		$("#student_edit_name").val(name_td.html());
		$("#student_edit_id").val(name_td.prev().html());
		student_edit.style.display = "block";
	} else {
		student_edit.style.display = "none";
	}
}

/**
 * 搜索
 * @param {DOM} 搜搜表单
 */
function searchStudent(form) {
	var input = form.search.value;
	if ($.trim(input) === "") {
		return false;
	}
	return true;
}