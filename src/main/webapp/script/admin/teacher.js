/**
 * [variables 全局变量]
 * @type {Object}
 */
var variables = {
	//已选班级是否已经改变
	hasClazzChanged: false,
	//已教班级记录
	clazzNotes: null,
	//当前活动的老师
	currentTeacher: 0,
	//缓存查询出的年级json
	grades: null
};

/**
 * [ClazzNote 已教的班级的记录，用以避免重复选择]
 * @param cno 班号
 */
function ClazzNote(clazzId, cno) {
	this.clazzId = clazzId;
	this.cno = cno;
}

/**
 * [equals 比较两个ClazzNote对象是否相等]
 * @param  {[ClazzNote]} note
 * @return {[boolean]}      [相等返回true]
 */
ClazzNote.prototype.equals = function(clazzId) {
	return this.clazzId == clazzId;
};

/**
 * [deleteByClazzId 按照班级id从数组中删除一个元素]
 * @return 返回被删除的元素
 */
ClazzNote.deleteByClazzId = function(clazzId) {
	//所找元素的下标
	var index = 0;
	for(index;index < variables.clazzNotes.length;index ++) {
		if(variables.clazzNotes[index].equals(clazzId)) {
			break;
		}
	}
	if(index < variables.clazzNotes.length) {
		var removed = variables.clazzNotes[index];	
		variables.clazzNotes.splice(index, 1);
		return removed;
	}
	return null;
}

/**
 * [contains clazzNotes数组是否包含此条记录]
 */
ClazzNote.contains = function(clazzId) {
	for(var i = 0;i < variables.clazzNotes.length;i ++) {
		if(variables.clazzNotes[i].equals(clazzId)) {
			return true;
		}
	}
	return false;
}

/**
 * [[删除单个元素]]
 * @param {[[DOM]]} btn [[触发此函数的按钮]]
 */
function deleteTeacher(btn) {
	var id = $(btn).parent().prev().prev().html();
	if(confirm("这会导致相应的教师及学生被删除,您确定?")) {
		sendDeleteRequest(id);
	}
}

/**
 * [[发送删除请求]]
 * @param {[[String]]} [[请求参数]]
 */
function sendDeleteRequest(id) {
	$.ajax({
		"url" : "admin/teacher/delete/" + id,
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
                    _resetTeacher(form.name, error);
                    Tips.showSuccess(json.message);
                }
            }
        });
    }
    return false;
}

/**
 * [toggleTeacherEdit 切换教师编辑界面]
 * @param  {Boolean} isShow [显示/隐藏]
 * @param  {[DOM]}  btn    [触发操作的按钮]
 */
function toggleTeacherEdit(isShow, btn) {
	var teacherEdit = document.getElementById("teacher_edit");
	if(isShow) {
		//设置教职工号
		var $nameTd = $(btn).parent().prev();
		$("#teacher_edit_id").val($nameTd.prev().html());
		//设置教师姓名
		$("#teacher_edit_name").val($nameTd.html());
		teacherEdit.style.display = "block";
	}else {
		document.getElementById("teacher_edit_error").innerHTML = "";
		teacherEdit.style.display = "none";
	}
}

/**
 * [editTeacher 教师编辑]
 * @param  {[DOM]} form [所在的表单]
 */
function editTeacher(form) {
	var error = document.getElementById("teacher_edit_error");
	var result = _checkTeacher(form, error);
	if(result.result) {
		$.ajax({
			"url": "admin/teacher/edit",
			"data": "id=" + result.id + "&name=" + result.name,
			"async": false,
			"dataType": "json",
			"success": function(json) {
				if(json.result == "0") {
					Tips.showError(json.message);
				}else if(json.result == "1") {
					toggleTeacherEdit(false);
					_resetTeacher(form.name, error);
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
	var teacherAdd = document.getElementById("teacher_add");
	if (isShow) {
		teacherAdd.style.display = "block";
	} else {
		document.getElementById("teacher_add_error").innerHTML = "";
		teacherAdd.style.display = "none";
	}
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

/**
 * [_handleJSON 处理json请求结果]
 * @param  {[Object]}   json [json结果]
 * @param  {[jquery]}   container [容器]
 * @param  {Function} callback  [回调函数，可以接受一个当前json对象的参数]
 * @param  {String} tipsOption  [提示选项，比如<option value='0'>年级...</option>, 可选]
 */
function _handleJSON(json, container, callback, tipsOption) {
	if(json.result == "0") {
		Tips.showError(json.message);
	}else if(json.result == "1") {
		container.empty();
		var options = new Array();
		if(tipsOption != undefined) {
			options.push(tipsOption);
		}
		for(var i = 0;i < json.data.length;i ++) {
			options.push(callback(json.data[i]));
		}
		container.append($(options.join("")));
	}
}

/**
 * [_loadTeachClazz 加载所教的班级]
 * @param  {[jquery对象]} $clazzList [班级列表容器]
 * @param [String] tid 教师id
 */
function _loadTeachClazz($clazzList, tid) {
	$.ajax({
		"url": "admin/teacher/clazz/list",
		"data": "tid=" + tid,
		"async": false,
		"dataType": "json",
		"success": function(json) {
			variables.clazzNotes = new Array();
			_handleJSON(json, $("#clazz_list"), function(element) {
				//添加已教班级记录
				variables.clazzNotes.push(new ClazzNote(element.id, element.cno));
				return "<li><input type='checkbox' name='clazzs' value='" + element.id + "'><span>" + element.grade.grade + "级" + 
				element.major.name + element.cno + "班</span></li>";
			});
		}
	});
}

/**
 * [_loadGrade 加载年级到年级列表]
 */
function _loadGrade() {
	$.ajax({
		"url": "grade/ajax",
		"async": false,
		"dataType": "json",
		"success": function(json) {
			variables.grades = json;
			_handleJSON(json, $("#grade_select"), function(grade) {
				return "<option value='" + grade.id + "'>" + grade.grade + "</option>";
			}, "<option value='0'>年级...</option>");
		}
	})
}

/**
 * [toggleClazzEdit 所教班级修改界面的显示/隐藏]
 * @param  {Boolean} isShow [是否显示]
 * @param  {[DOM]}  btn    [触发的按钮]
 */
function toggleClazzEdit(isShow, btn) {
	var clazzEdit = document.getElementById("teacher_clazz_edit");
	if(isShow) {
		var $clazzList = $("#clazz_list");
		$clazzList.append("<li>正在努力加载...</li>");
		clazzEdit.style.display = "block";
		//加载已教班级
		var tid = $(btn).parent().prev().prev().html();
		variables.currentTeacher = tid;
		_loadTeachClazz($clazzList, tid);
		//加载年级
		_loadGrade();
	}else {
		_resetClazzEdit();
		clazzEdit.style.display = "none";
	}
}

/**
 * [_resetClazzEdit 重置班级修改界面]
 */
function _resetClazzEdit() {
	$("#grade_select option:gt(0)").remove();
	$("#clazz_select option:gt(0)").remove();
	$("#major_select option:gt(0)").remove();
	$("#clazz_error").html("&nbsp;");
}

/**
 * [removeClazz 移除选中的班级]
 */
function removeClazz() {
	var $checkeds = $("input:checkbox[name=clazzs]:checked");
	if($checkeds.length > 0) {
		variables.hasClazzChanged = true;
		var $clazzSelect = $("#clazz_select");
		var $this = null;
		$checkeds.each(function() {
			$this = $(this);
			//将此条记录从已教班级索引删除，并且加入班级下拉列表
			var removed = ClazzNote.deleteByClazzId($this.val());
			$this.next().remove();
			$this.remove();
		});
	}
	resetSelectes($("grade_select"), $("#major_select"), $clazzSelect)
}

/**
 * [_loadMajor 加载专业]
 * @param  {[jquery]} $majorSelect [专业下拉列表]
 * @param  {[int]} gradeId      [年级id]
 */
function _loadMajor($majorSelect, gradeId) {
	//如果年级选择的是空选项，那么清空专业
	if (gradeId === "0") {
		$majorSelect.empty().append("<option value='0'>专业...</option>");
	} else {
		$.ajax({
			"url": "major/ajax",
			"data": "grade=" + gradeId,
			"async": false,
			"dataType": "json",
			"success": function(json) {
				_handleJSON(json, $majorSelect, function(element) {
					return "<option value='" + element.id + "'>" + element.name + "</option>";
				}, "<option value='0'>专业...</option>");
			}
		});
	}
}

/**
 * [changeMajor 跟随年级的变化联动修改专业]
 * @param  {[DOM]} gradeSelect [年级下拉选单]
 */
function changeMajor(gradeSelect) {
	var gradeId = $(gradeSelect).val();
	_loadMajor($("#major_select"), gradeId);
}

/**
 * [_loadClazz 加载班级]
 * @param  {[jquery]} $clazzSelect [班级列表]
 */
function _loadClazz(gradeId, majorId, $clazzSelect) {
	if (majorId === "0") {
		$clazzSelect.empty().append("<option value='0'>班级...</option>");
	} else {
		$.ajax({
			"url": "clazz/ajax",
			"data": "grade=" + gradeId + "&major=" + majorId,
			"async": false,
			"dataType": "json",
			"success": function(json) {
				_handleJSON(json, $clazzSelect, function(element) {
					//在已教列表中不存在才会添加进入列表
					if(ClazzNote.contains(element.id)) {
						return "";
					}else {
						return "<option value='" + element.id + "'>" + element.cno + "班</option>";
					}
				}, "<option value='0'>班级...</option>");
			}
		});
	}
}

/**
 * [changeClazz 班级随专业和年级联动]
 * @param  {[DOM]} majorSelect [专业列表]
 */
function changeClazz(majorSelect) {
	var gradeId = $("#grade_select").val();
	var majorId = $(majorSelect).val();
	_loadClazz(gradeId, majorId, $("#clazz_select"));
}

function _checkClazz($gradeSelect, $majorSelect) {
	var $error = $("#clazz_error");
	if($gradeSelect.val() == "0") {
		$error.html("请选择年级");
		return false;
	}
	if($majorSelect.val() == "0") {
		$error.html("请选择专业");
		return false;
	}
	return true;
}

/**
 * [addClazz 把下拉列表选中的班级添加到"已教班级"中，班级可以不选，这样的话会添加当前年级、专业下的所有班级]
 */
function addClazz() {
	var $gradeSelect = $("#grade_select");
	var $majorSelect = $("#major_select");
	var $clazzSelect = $("#clazz_select");
	if(_checkClazz($gradeSelect, $majorSelect)) {
		var clazzId = $clazzSelect.val();
		//被选中的年级和专业option元素
		var $selectedGrade = $("#grade_select option:selected");
		var $selectedMajor = $("#major_select option:selected");
		//结果数组，直接可以append到已教列表
		var result = new Array();
		var $element = null;
		var temp = "";
		if(clazzId === "0") {
			if(confirm("您没有选择班级,这会导致所有班级会被加入,您确定?")) {
				var $clazzs = $("#clazz_select option:gt(0)");
				$clazzs.each(function() {
					$element = $(this);
					temp = "<li><input type='checkbox' name='clazzs' value='" + $element.val() + 
						"'><span>" + $selectedGrade.html() + "级" + $selectedMajor.html() + $element.html() + "</span></li>";
					result.push(temp);
				});
			}
		}else {
			var $selectedClazz = $clazzSelect.children("option:selected");
			temp = "<li><input type='checkbox' name='clazzs' value='" + $selectedClazz.val() + 
						"'><span>" + $selectedGrade.html() + "级" + $selectedMajor.html() + $selectedClazz.html() + "</span></li>";
			result.push(temp);
			//添加到已教班级列表后，应该从班级下拉列表删除
			$selectedClazz.remove();
			variables.clazzNotes.push(new ClazzNote($selectedClazz.val(), ""));
		}
		variables.hasClazzChanged = true;
		$("#clazz_list").append($(result.join("")));
		//添加完成后，复位所有选择器
		resetSelectes($gradeSelect, $majorSelect, $clazzSelect);
	}
}

/**
 * 复位各个下拉列表
 * @param $gradeSelect
 * @param $majorSelect
 * @param $clazzSelect
 */
function resetSelectes($gradeSelect, $majorSelect, $clazzSelect) {
	$majorSelect.empty().append("<option value='0'>专业...</option>");
	$clazzSelect.empty().append("<option value='0'>班级...</option>");
	_handleJSON(variables.grades, $gradeSelect, function(grade) {
			return "<option value='" + grade.id + "'>" + grade.grade + "</option>";
		}, "<option value='0' selected>年级...</option>");
}

/**
 * [save 保存]
 */
function save() {
	var clazzIds = new Array();
	$("input:checkbox[name=clazzs]").each(function() {
		clazzIds.push(this.value);
	});
	$.ajax({
		"url": "admin/teacher/clazz/save",
		"data": "ids=" + clazzIds.join() + "&tid=" + variables.currentTeacher,
		"async": false,
		"success": function(json) {
			if(json.result == "0") {
				Tips.showError(json.message);
			}else if(json.result == "1") {
				toggleClazzEdit(false);
				Tips.showSuccess(json.message);
			}
		}
	});
}