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
function deleteTeacheres() {
	var checkboxes = $("input:checkbox[name=cb]:checked");
	if (checkboxes.length == 0) {
		alert("请选择您要删除的记录");
		return false;
	}
	if (confirm("这会导致相应的教师及学生被删除,您确定?")) {
		//拼接参数数组，结果如下1,2,3
		var ids = new Array();
		for (var i = 0; i < checkboxes.length; i++) {
			//TODO
		}
		sendDeleteRequest(ids.join());
	}
}

/**
 * [[删除单个元素]]
 * @param {[[DOM]]} btn [[触发此函数的按钮]]
 */
function deleteTeacher(btn) {
	var id = $(btn).parent().prev().prev().html();
	if(confirm("这会导致相应的教师及学生被删除,您确定?")) {
		sendDeleteRequest("ids=" + id);
	}
}

/**
 * [[发送删除请求]]
 * @param {[[String]]} [[请求参数]]
 */
function sendDeleteRequest(params) {
	$.ajax({
		"url" : "admin/teacher/delete",
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
 * [[教师增加校验]]
 * @param {[[DOM]]} form [[要检查的表单]]
 * @param {[[DOM]]} error [[显示错误的DOM]]
 * @return CheckResult
 */
function _checkTeacher(form, error) {
	//校验教职工号
	var id = form.id;
	var idValue = id.value.trim();
	if(idValue == "") {
		error.innerHTML = "请输入教职工号";
		id.focus();
		return new CheckResult(false);
	}
	//校验姓名
	var name = form.name;
	var nameValue = name.value.trim();
	if(nameValue == "") {
		error.innerHTML = "请输入姓名";
		name.focus();
		return new CheckResult(false);
	}
	return new CheckResult(true, idValue, nameValue);
}

/**
 * [CheckResult 封装校验结果]
 * @param {[boolean]} result [成功与否]
 * @param {[String]} id     [教职工号]
 * @param {[String]} name   [经过处理的姓名]
 */
function CheckResult(result, id, name) {
	this.result = result;
	this.id = id;
	this.name = name;
}

/**
 * [[添加教师]]
 * @param {[[DOM]]} form [[所在的表单]]
 */
function addTeacher(form) {
    var error = document.getElementById("teacher_add_error");
    var checkResult = _checkTeacher(form, error);
    if(checkResult.result) {
        $.ajax({
            "url": "admin/teacher/add",
            "data": "id=" + checkResult.id + "&name=" + checkResult.name,
            "async": false,
            "dataType": "json",
            "success": function(json) {
                if(json.result == 0) {
                    error.innerHTML = json.message;
                }else {
                    toggleTeacherAdd(false);
                    _resetTeacher(form.teacher, error);
                    Tips.showSuccess(json.message);
                }
            }
        });
    }
    return false;
}

/**
 * [[显示/隐藏教师添加窗口]
 */
function toggleTeacherAdd(isShow) {
	document.getElementById("teacher_add").style.display = isShow ? "block"
			: "none";
}

/**
 * [[重置教师输入界面]]
 * @param {[[DOM]]} Teacher [[教师输入]]
 * @param {[[DOM]]} error [[错误显示]]
 */
function _resetTeacher(teacher, error) {
    teacher.value = "";
    error.innerHTML = "";
}

/**
 * 根据教职工号和姓名搜索
 * 如果教职工号和姓名都为空，那么不发送请求
 * 如果已填写教职工号，那么姓名不予理会
 * @param {DOM} form 搜索表单
 */
function search(form) {
	var id = form.id.value.trim();
	var name = form.name.value.trim();
	return id != "" || name != "";
}