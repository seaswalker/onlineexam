/**
 * [[删除单个元素]]
 * @param {[[DOM]]} btn [[触发此函数的按钮]]
 */
function deleteClazz(btn) {
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
		"url" : "admin/clazz/delete/" + id,
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
 * [[班级增加校验]]
 * @param {[[DOM]]} form [[要检查的表单]]
 * @param {[[DOM]]} error [[显示错误的DOM]]
 */
function _checkClazz(form, error) {
	//校验年级
	if($(form.grade).val() == "0") {
		error.innerHTML = "请选择所在的年级";
		return false;
	}
	//校验专业
	if($(form.major).val() == "0") {
		error.innerHTML = "请选择所在专业";
		return false;
	}
	var clazz = form.clazz;
	var clazz_value = clazz.value.trim();
	if (clazz_value == "") {
		error.innerHTML = "请输入班级名称";
		clazz.focus();
		return false;
	}
	//检查是否是数字
	var pattern = new RegExp("^[1-9][0-9]*$");
	if(!clazz_value.match(pattern)) {
		error.innerHTML = "格式非法，请输入纯数字";
		clazz.focus();
		return false;
	}
	return true;
}

/**
 * [changeMajor 动态加载专业]
 * @param  {[DOM]} gradeSelect [年级下拉列表]
 */
function changeMajor(gradeSelect) {
	var value = $(gradeSelect).val();
	if(value != "0") {
		//专业下拉列表
		var $majorSelect = $("#major_select");
		$.ajax({
			"url": "major/ajax",
			"async": false,
			"dataType": "json",
			"success": function(json) {
				if(json.result == "0") {
					Tips.showError(json.message);
				}else if(json.result == "1") {
					var options = new Array();
					var option = null;
					for(var i = 0;i < json.data.length;i ++) {
						option = json.data[i];
						options.push("<option value='" + option.id + "'>" + option.name + "</option>");
					}
					$majorSelect.append($(options.join("")));
				}
			}
		});
	}
}

/**
 * [[添加班级]]
 * @param {[[DOM]]} form [[所在的表单]]
 */
function addClazz(form) {
    var error = document.getElementById("clazz_add_error");
    if(_checkClazz(form, error)) {
        $.ajax({
            "url": "admin/clazz/add",
            "data": "grade=" + $(form.grade).val() + "&major=" + $(form.major).val() + "&clazz=" + form.clazz.value,
            "async": false,
            "dataType": "json",
            "success": function(json) {
                if(json.result == 0) {
                    error.innerHTML = json.message;
                }else {
                    toggleClazzAdd(false);
                    _resetClazz(form.clazz, error);
                    Tips.showSuccess(json.message);
                    window.location.reload();
                }
            }
        });
    }
    return false;
}

/**
 * [[显示/隐藏班级添加窗口]
 */
function toggleClazzAdd(isShow) {
	document.getElementById("clazz_add").style.display = isShow ? "block"
			: "none";
}

/**
 * [[重置班级输入界面]]
 * @param {[[DOM]]} Clazz [[班级输入]]
 * @param {[[DOM]]} error [[错误显示]]
 */
function _resetClazz(clazz, error) {
    clazz.value = "";
    error.innerHTML = "";
}

/**
 * 根据年级和专业搜索
 * 如果年级和专业都没有选择，那么不发送请求
 * @param {DOM} form 搜索表单
 */
function search(form) {
	if ($(form.grade).val() == "0" && $(form.major).val() == "0") {
		return false;
	}
	return true;
}