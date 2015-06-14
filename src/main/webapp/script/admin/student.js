/**
 * [全选]
 * @param  {[DOM]} checkbox [全选按钮]
 */
function chooseAll(checkbox) {
	var checkboxes = $("input:checkbox[name=cb]");
	var isCheck = checkbox.checked;
	if (isCheck) {
		checkboxes.prop("checked", true);
	} else {
		checkboxes.removeAttr('checked');
	}
}

/**
 * 批量删除元素
 */
function deleteStudents() {
	var checkboxes = $("input:checkbox[name=cb]:checked");
	if (checkboxes.length == 0) {
		alert("请选择您要删除的记录");
		return false;
	}
	if (confirm("这会导致相应的班级及学生被删除,您确定?")) {
		//拼接参数数组，结果如下1,2,3
		var ids = new Array();
		for (var i = 0; i < checkboxes.length; i++) {
			ids.push($(checkboxes[i]).parent().next().html());
		}
		sendDeleteRequest(ids.join());
	}
}

/**
 * [[删除单个元素]]
 * @param {[[DOM]]} btn [[触发此函数的按钮]]
 */
function deleteStudent(btn) {
	var id = $(btn).parent().prev().prev().html();
	if(confirm("这会导致相应的班级及学生被删除,您确定?")) {
		sendDeleteRequest("ids=" + id);
	}
}

/**
 * [[发送删除请求]]
 * @param {[[String]]} [[请求参数]]
 */
function sendDeleteRequest(params) {
	$.ajax({
		"url" : "student/delete",
		"data" : "ids=" + ids.join(),
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
 * [[检查输入的专业]]
 * @param {[[DOM]]} student [[专业输入input元素]]
 * @param {[[DOM]]} error [[显示错误的DOM]]
 */
function _checkStudent(student, student_value, error) {
	if (student_value == "") {
		error.innerHTML = "请输入年级";
		student.focus();
		return false;
	}
	//检查是否是数字
	var pattern = new RegExp("^[1-9][0-9]*$");
	if(!student_value.match(pattern)) {
		error.innerHTML = "格式有误，示例2012";
		student.focus();
		return false;
	}
	return true;
}

/**
 * [[添加专业]]
 * @param {[[DOM]]} form [[所在的表单]]
 */
function addStudent(form) {
    var student = form.student;
    var student_value = student.value.trim();
    var error = document.getElementById("student_add_error");
    if(_checkStudent(student, student_value, error)) {
        $.ajax({
            "url": "admin/student/add",
            "data": "student=" + student_value,
            "async": false,
            "dataType": "json",
            "success": function(json) {
                if(json.result == 0) {
                    error.innerHTML = json.message;
                }else {
                    toggleStudentAdd(false);
                    _resetStudent(student, error);
                    Tips.showSuccess(json.message);
                }
            }
        });
    }
    return false;
}

/**
 * 编辑专业
 * @param {} form
 */
function editStudent(form) {
	var student = form.student;
    var student_value = student.value.trim();
    var error = document.getElementById("student_edit_error");
    if(_checkStudent(student, student_value, error)) {
        $.ajax({
            "url": "admin/student/edit",
            "data": "student=" + student_value + "&id=" + form.id.value,
            "async": false,
            "dataType": "json",
            "success": function(json) {
                if(json.result == 0) {
                    error.innerHTML = json.message;
                }else {
                    toggleStudentEdit(false);
                    _resetStudent(student, error);
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
		$.ajax({
			"url": "grade/ajax",
			"dataType": "json",
			"async": false,
			"success": function(json) {
				if(json.result == "1") {
					$("#grade_select").empty();
					var options = new Array();
					options.push("<option value='0'>年级...</option>");
					for(var i = 0;i < json.data.length;i ++) {
						options.push("<option value=" + json.data[i].id + ">" + json.data[i].grade + "</option>");
					}
					$("#grade_select").append($(options.join("")));
				}
			}
		});
		student_add.style.display = "block";
	}else {
		student_add.style.display = "none";
	}
}

/**
 * 当年级改变时联动专业
 * @param {DOM} select 年级下拉列表
 */
function changeMajor(select) {
	var grade = $(select).val();
	//年级选单
	var $major_select = $("#major_select");
	$.ajax({
		"url": "major/ajax",
		"data": "grade=" + grade,
		"dataType": "json",
		"async": false,
		"success": function(json) {
			if(json.result == "0") {
				Tips.showError(json.message);
			}else if(json.result == "1") {
				$major_select.empty();
				//专业数组
				var options = new Array();
				options.push("<option value='0'>专业...</option>");
				for(var i = 0;i < json.data.length;i ++) {
					options.push("<option value='" + json.data[i].id + "'>" + json.data[i].name + "</option>");
				}
				$major_select.append($(options.join("")));
			}
		}
	});
}

/**
 * 专业变化时，根据年级和专业查出班级
 * @param {} select 专业下拉列表
 */
function changeClazz(select) {
	var major = $(select).val();
	var grade = $("#grade_select").val();
	var $clazz_select = $("#clazz_select");
	$.ajax({
		"url": "clazz/ajax",
		"data": "grade=" + grade + "&major=" + major,
		"async": false,
		"dataType": "json",
		"success": function(json) {
			if(json.result == "0") {
				Tips.showError(json.message);
			}else if(json.result == "1") {
				$clazz_select.empty();
				var options = new Array();
				options.push("<option value='0'>班级...</option>");
				for(var i = 0;i < json.data.length;i ++) {
					options.push("<option value='" + json.data[i].id + "'>" + json.data[i].cno + "班</option>");
				}
				$clazz_select.append($(options.join("")));
			}
		}
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
		var name_td = $(btn).parent().prev();
		$("#student_edit_student").val(name_td.html());
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
function search(form) {
	var input = form.search.value;
	if(input.trim() == "") {
		return false;
	}
	return true;
}