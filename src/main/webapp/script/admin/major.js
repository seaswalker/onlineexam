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
function deleteElements() {
	var checkboxes = $("input:checkbox[name=cb]:checked");
	if (checkboxes.length == 0) {
		alert("请选择您要删除的记录");
		return false;
	}
	if (confirm("您确定删除选中的记录?")) {
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
function deleteElement(btn) {
	var id = $(btn).parent().prev().prev().html();
	sendDeleteRequest("ids=" + id);
}

/**
 * [[发送删除请求]]
 * @param {[[String]]} [[请求参数]]
 */
function sendDeleteRequest(params) {
	$.ajax({
		"url" : "major/delete",
		"params" : "ids=" + ids.join(),
		"dataType" : "json",
		"async" : false,
		"success" : function() {
			//TODO
		}
	});
}

/**
 * [[检查输入的专业]]
 * @param {[[DOM]]} form [[被检查的表单元素]]
 */
function checkMajor(form) {
	var major = form.major;
	if (major.value.trim() == "") {
		var error = document.getElementById("major_error");
		error.innerHTML = "请输入专业名称";
		major.focus();
		return false;
	}
	return true;
}

/**
 * [[显示/隐藏专业添加窗口]
 */
function toggleMajorAdd(isShow) {
	document.getElementById("major_add").style.display = isShow ? "block"
			: "none";
}

/**
 * [[显示/隐藏专业编辑窗口]]
 * @param {[[boolean]} [[true--显示]]
 * @param {[[DOM]]} [[如果是显示，那么为触发的按钮]]
 */
function toggleMajorEdit(isShow, btn) {
	var major_edit = document.getElementById("major_edit");
	if (isShow) {
		var name_td = $(btn).parent().prev();
		$("#major_edit_major").val(name_td.html());
		$("#major_edit_id").val(name_td.prev().html());
		major_edit.style.display = "block";
	} else {
		major_edit.style.display = "none";
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