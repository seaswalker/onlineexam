/**
 * [[删除单个元素]]
 * @param {[[DOM]]} btn [[触发此函数的按钮]]
 */
function deleteGrade(btn) {
	var id = $(btn).parent().prev().prev().html();
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
		"url" : "admin/grade/delete/" + id,
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
 * @param {[[DOM]]} grade [[专业输入input元素]]
 * @param {[[DOM]]} error [[显示错误的DOM]]
 */
function _checkGrade(grade, grade_value, error) {
	if (grade_value == "") {
		error.innerHTML = "请输入年级";
		grade.focus();
		return false;
	}
	//检查是否是数字
	var pattern = new RegExp("^[1-9][0-9]*$");
	if(!grade_value.match(pattern)) {
		error.innerHTML = "格式有误，示例2012";
		grade.focus();
		return false;
	}
	return true;
}

/**
 * [[添加专业]]
 * @param {[[DOM]]} form [[所在的表单]]
 */
function addGrade(form) {
    var grade = form.grade;
    var grade_value = grade.value.trim();
    var error = document.getElementById("grade_add_error");
    if(_checkGrade(grade, grade_value, error)) {
        $.ajax({
            "url": "admin/grade/add",
            "data": "grade=" + grade_value,
            "async": false,
            "dataType": "json",
            "success": function(json) {
                if(json.result == 0) {
                    error.innerHTML = json.message;
                }else {
                    toggleGradeAdd(false);
                    _resetGrade(grade, error);
                    Tips.showSuccess(json.message);
                    setTimeout(function() {
                    	window.location.reload();
                    }, 2000);
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
function editGrade(form) {
	var grade = form.grade;
    var grade_value = grade.value.trim();
    var error = document.getElementById("grade_edit_error");
    if(_checkGrade(grade, grade_value, error)) {
        $.ajax({
            "url": "admin/grade/edit",
            "data": "grade=" + grade_value + "&id=" + form.id.value,
            "async": false,
            "dataType": "json",
            "success": function(json) {
                if(json.result == 0) {
                    error.innerHTML = json.message;
                }else {
                    toggleGradeEdit(false);
                    _resetGrade(grade, error);
                    Tips.showSuccess(json.message);
                    window.location.href = "admin/grade/list";
                }
            }
        });
    }
    return false;
}

/**
 * [[显示/隐藏专业添加窗口]
 */
function toggleGradeAdd(isShow) {
	document.getElementById("grade_add").style.display = isShow ? "block"
			: "none";
}

/**
 * [[重置专业输入界面]]
 * @param {[[DOM]]} grade [[专业输入]]
 * @param {[[DOM]]} error [[错误显示]]
 */
function _resetGrade(grade, error) {
    grade.value = "";
    error.innerHTML = "";
}

/**
 * [[显示/隐藏专业编辑窗口]]
 * @param {[[boolean]} [[true--显示]]
 * @param {[[DOM]]} [[如果是显示，那么为触发的按钮]]
 */
function toggleGradeEdit(isShow, btn) {
	var grade_edit = document.getElementById("grade_edit");
	if (isShow) {
		var name_td = $(btn).parent().prev();
		$("#grade_edit_grade").val(name_td.html());
		$("#grade_edit_id").val(name_td.prev().html());
		grade_edit.style.display = "block";
	} else {
		grade_edit.style.display = "none";
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