/**
 * 此js是一个反面教材，大部分函数都在全局作用域
 * [ExamDesign 试卷设计全局变量]
 * @type {Object}
 */
var ExamDesign = {
	//单选题序号
	singleQuestionIndex: 2,
	//多选题序号
	multiQuestionIndex: 2,
	//判断题序号
	judgeQuestionIndex: 2,
	//正则表达式校验数字
	numberRegExp: new RegExp("^[1-9][0-9]*$"),
	//题库的题目类型，其对应关系参见addQuestionFromBank函数的注释
	questionBankType: 0,
	//校验器缓存，checkAll方法使用
	validators: {
		requireds: null,
		numbers: null,
		selects: null,
		multiAnswers: null
	},
	//缓存题库中已经被添加的题目的id，防止重复添加同一道题目
	questionCache: {
		singles: [],
		multis: [],
		judges: []
	},
	//缓存此教师所教的班级数组
	classes: null
};

$(function() {
	curTime();
	setListeners();
	//设置输入校验
	setValidators();
	loadClasses();
});

/**
 * 加载此教师所教的所有班级,返回的json格式示例:
 * {"result":"1","data":[{"gid":"1","grade":"2012","mid":"62","major":"电子信息科学与技术","cid":"1","cno":"2"}]}
 */
function loadClasses() {
	$.post("teacher/classes", null, function(data) {
		if (data.result === "1") {
			ExamDesign.classes = data.data;
			var str = "", e, loadedGrades = [];
			for (var i = 0, l = data.data.length;i < l;i ++) {
				e = data.data[i];
				if ($.inArray(e.gid, loadedGrades) === -1) {
					str += "<option value='" + e.gid + "'>" + e.grade + "级</option>";
					loadedGrades.push(e.gid);
				}
			}
			$(str).appendTo($("#grade_select"));
		}
	}, "json");
}

function loadMajor(gradeSelect) {
	var $majorSelect = $("#major_select").empty();
	var id = $(gradeSelect).val();
	if (id === "0") {
		$majorSelect.append("<option value='0'>专业...</option>");
	} else {
		var str = "<option value='0'>专业...</option>", e;
		//因为缓存是以班级组织的，所以可能加载重复的专业，所以记录下已经加载的专业
		var loadedMajors = [];
		for (var i = 0, l = ExamDesign.classes.length;i < l;i ++) {
			e = ExamDesign.classes[i];
			if (e.gid === id) {
				if ($.inArray(e.mid, loadedMajors) === -1) {
					str += "<option value='" + e.mid + "'>" + e.major + "</option>";
					loadedMajors.push(e.mid);
				}
			}
		}
		$(str).appendTo($majorSelect);
	}
}

function loadClazz(majorSelect) {
	var mid = $(majorSelect).val();
	var $clazzSelect = $("#clazz_select").empty();
	if (mid === "0") {
		$clazzSelect.append("<option value='0'>班级...</option>");
	} else {
		var str = "<option value='0'>班级...</option>", e;
		var loadedClasses = [];
		for (var i = 0, l = ExamDesign.classes.length;i < l;i ++) {
			e = ExamDesign.classes[i];
			if (e.mid === mid) {
				if ($.inArray(e.cid, loadedClasses) === -1) {
					str += "<option value='" + e.cid + "'>" + e.cno + "班</option>";
					loadedClasses.push(e.cid);	
				}
			}
		}
		$(str).appendTo($clazzSelect);
	}
}

/**
 * [setValidators 设置输入校验]
 */
function setValidators() {
	//设置非空校验器
	var requireds = 
		$("#single_questions input[class=required], #multi_questions input[class=required], #judge_questions input[class=required], #other-setting input[class=required]").toArray(),
		$this;
	$.each(requireds, function() {
		$this = $(this);
		$this.blur(function(event) {
			notNullCheck(event.target);
		});
		$this.focus(clearErrorInfo);
	})
	//设置非空校验缓存
	ExamDesign.validators.requireds = {
		elements: requireds,
		callback: notNullCheck
	};
	//设置数字校验
	var numbers = 
		$("#single_questions input[class=number], #multi_questions input[class=number], #judge_questions input[class=number], #other-setting input[class=number]").toArray();
	$.each(numbers, function() {
		$this = $(this);
		$this.blur(function(event) {
			numberCheck(event.target);
		});	
		$this.focus(clearErrorInfo);
	});
	//删除最后一个元素，因为最后一个是运行时间，默认是不显示的
	numbers.splice(numbers.length - 1, 1);
	//设置数组校验器缓存
	ExamDesign.validators.numbers = {
		elements: numbers,
		callback: numberCheck
	};
    //设置下拉列表校验器
    var $gradeSelect = $("#grade_select");
    $gradeSelect.blur(function(event) {
    	selectCheck.call(this, event.target, "请选择年级");
    }).focus(clearErrorInfo);
    var $majorSelect = $("#major_select");
    $majorSelect.blur(function(event) {
    	selectCheck.call(this, event.target, "请选择专业");
    }).focus(clearErrorInfo);
    //设置下拉列表校验器缓存
    ExamDesign.validators.selects = {
    	elements: [
    		{
    			element: $gradeSelect[0],
    			message: "请选择年级"
    		},
    		{
    			element: $majorSelect[0],
    			message: "请选择专业"
    		}
    	],
    	callback: selectCheck
    };
    //多选按钮错误信息清除
	$("#multi_questions td[name=multi-options-container]").each(function() {
		$(this).click(function(event) {
			//此处target指向的是input元素
			$(event.currentTarget).next().find("span").html("");
		});	
	});
}

/**
 * [setListeners 为按钮加入监听器]
 */
function setListeners() {
	//设置移除题目监听器
	$("button[name=remove-single-btn]").click(removeSingleQuestion);
	$("button[name=remove-multi-btn]").click(removeMultiQuestion);
	$("button[name=remove-judge-btn]").click(removeJudgeQuestion);
	//设置添加问题监听器
	$("#add-single-btn").click(addSingleQuestion);
	$("#add-multi-btn").click(addMultiQuestion);
	$("#add-judge-btn").click(addJudgeQuestion);
	//题库显示按钮
	var $btn;
	$("button[name=show-bank-btn]").click(function(event) {
		$btn = $(event.target);
		toggleBank($btn.attr("questionType"), true);
	});	
	//关闭题库
	$("#hide-bank").click(function() {
		toggleBank(0, false);
	});
	//从题库添加题目
	$("#add-question-bank-btn").click(addQuestionFromBank);
	//运行时间输入框切换
	$("input[name=status]").each(function(index) {
		$(this).click(function() {
			toggleRunTime(index === 1);
		});
	});
	//保存按钮
	$("#save-btn").click(checkAll);
	//设置专业和班级加载器
	$("#grade_select").change(function() {
		loadMajor(this);
	});
	$("#major_select").change(function() {
		loadClazz(this);
	});
}

/**
 * [getErrorElement 获得此input元素对应的错误信息显示元素]
 * @param  {[jquery]} element [jquery对象]
 */
function getErrorElement(element) {
	return element.parent().next().find(".error");
}

/**
 * [checkAll 触发所有校验器]
 * @return [无返回值，验证通过后提交]
 */
function checkAll() {
	//检查是否有题
	if (ExamDesign.singleQuestionIndex === 1 && ExamDesign.multiQuestionIndex === 1 && ExamDesign.judgeQuestionIndex === 1) {
		alert("请不要提交空试卷!");
		return;
	}
	//触发非空校验
	var cb = ExamDesign.validators.requireds.callback,
		es = ExamDesign.validators.requireds.elements,
		e, $this;
	for (var i = 0, length = es.length;i < length;i ++) {
		if (!cb.call(this, es[i])) {
			return;
		}
	}
	//触发数字校验器
	cb = ExamDesign.validators.numbers.callback;
	es = ExamDesign.validators.numbers.elements;
	for (i = 0, length = es.length;i < length;i ++) {
		if (!cb.call(this, es[i])) {
			return;
		}
	}
	//触发下拉列表校验器
	cb = ExamDesign.validators.selects.callback;
	es = ExamDesign.validators.selects.elements;
	for (i = 0, length = es.length;i < length;i ++) {
		e = es[i];
		if (!cb.call(this, e.element, e.message)) {
			return;
		}
	}
	//校验多选题答案
    var containers = $("#multi_questions td[name=multi-options-container]").toArray();
    for (i = 0, length = containers.length;i < length;i ++) {
    	$this = $(containers[i]);
    	if ($this.find("input:checked").length < 2) {
    		$this.next().find("span").html("请至少选择两个答案");
			return;
    	}
    }
    //提交
    submit();
}

/**
 * [submit 将页面上的题目整理为json格式提交服务器]
 * json的格式如下:
 * var result = {
		singles: [
			{
				id: id是必要的，因为如果有id存在，那就意味着此题从题库添加而来，数据库存在此问题不需要再次保存，只需要建立关联关系
				title: "something",
				optionA: "something",
				optionB: "something",
				optionC: "something",
				optionD: "something",
				answer: "something",
				point: 10
			}
		],
		multis: [
			格式同上
		],
		judges: [
			{
				title: "something",
				answer: "something",
				point: 10
			}
		],
		setting: {
			timeLimit: 10,
			//年级id
			gid: 1,
			//专业id
			mid: ,
			//专业id，如果为空，那么说明添加一个gid年级mid专业下的所有班级
			cid: 2,
			//是否保存后立即运行
			status: 1,
			runTime: 10
		}
	};
 */
function submit() {
	var result = {
		singles: [],
		multis: [],
		judges: [],
		setting: {}
	};

	/**
	 * [generateQuestion 根据题目类型生成一个问题对象，格式参见submit函数注释]
	 * @param  {[jQuery]} $container [题目所在div容器]
	 * @param  {[Number]} type       [题目类型]
	 * @param  {[Function]} fn       [答案的获取方式，每种题型均不同]
	 * @return {[Object]}            [生成的题目对象]
	 */
	function generateQuestion($container, type, fn) {
		//题目type和名称对应关系
		var reflection = {
			1: "single",
			2: "multi",
			3: "judge"
		};
		var question = {}, typeStr = reflection[type];
		question.id = $container.find("input[name=question-id]").val();
		question.title = $container.find("input[name=" + typeStr + "_title]").val().replace(/"/g, "'");
		question.point = $container.find("input[name=" + typeStr + "_point]").val().replace(/"/g, "'");
		//判断题不需要选项
		if (type < 3) {
			question.optionA = $container.find("input[name=" + typeStr + "_optionA]").val().replace(/"/g, "'");
			question.optionB = $container.find("input[name=" + typeStr + "_optionB]").val().replace(/"/g, "'");
			question.optionC = $container.find("input[name=" + typeStr + "_optionC]").val().replace(/"/g, "'");
			question.optionD = $container.find("input[name=" + typeStr + "_optionD]").val().replace(/"/g, "'");
		}
		//默认当作单选题
		if (fn == null) {
			question.answer = $container.find("td[name=" + typeStr + "-options-container] input:checked").val();
		} else {
			question.answer = fn.call($container);
		}
		return question;
	}

	//单选题
	var $singleQuestions = $("#single_questions div[class=single_question]");
	$singleQuestions.each(function() {
		result.singles.push(generateQuestion($(this), 1));
	});
	//多选题
	var $multiQuestions = $("#multi_questions div[class=multi_question]");
	$multiQuestions.each(function() {
		result.multis.push(generateQuestion($(this), 2, function() {
			var str = "";
			$(this).find("td[name=multi-options-container] input:checked").each(function() {
				str += ($(this).val() + ",");
			});
			return str.substring(0, str.length - 1);
		}));
	});
	//判断题
	var $judgeQuestions = $("#judge_questions div[class=judge_question]");
	$judgeQuestions.each(function() {
		result.judges.push(generateQuestion($(this), 3, function() {
			return $(this).find("input:checked").val();
		}));	
	});
	//设置
	var $otherSetting = $("#other-setting"), gid, mid;
	result.setting.timeLimit = $otherSetting.find("input[name=time_limit]").val();
	gid = result.setting.gid = $("#grade_select").val();
	mid = result.setting.mid = $("#major_select").val();
	result.setting.cid = $("#clazz_select").val();
	//如果班级id为0(没有选中班级)，那么说明是添加专业下的所有班级，此时cid的格式应为1, 2, 3
	if (result.setting.cid === "0") {
		var str = "", e;
		for (var i = 0, l = ExamDesign.classes.length;i < l;i ++) {
			e = ExamDesign.classes[i];
			if (e.gid === gid && e.mid === mid) {
				str += (e.cid + ",");
			}
		}
		result.setting.cid = str.substring(0, str.length - 1);
	}
	result.setting.title = $otherSetting.find("input[name=exam_title]").val().replace(/"/g, "'");
	if ($otherSetting.find("input:checked").val() === "1") {
		result.setting.status = 1;
		result.setting.runTime = $otherSetting.find("input[name=run_time]").val();
	} else {
		result.setting.status = 0;
	}
	$.post("teacher/exam/save", {exam: JSON.stringify(result)}, function(data) {
		if (data.result === "1") {
			Tips.showSuccess("添加成功");
			setTimeout(function() {
				window.location.href = document.getElementById("base-path").value + "teacher/exam/list";
			}, 3000);
		} else {
			Tips.showError(data.message);
		}
	}, "json");
}

/**
 * [notNullCheck 非空检查]
 * 条件:触发此事件的元素后面有一个错误显示元素
 * @param  {[dom]} input [触发事件的dom元素]
 */
function notNullCheck(input) {
	var $input = $(input);
	var $error = getErrorElement($input);
	if($input.val().trim() == "") {
		$error.html("此字段不能为空");
		return false;
	}
	$error.html("");
	return true;
}

/**
 * [clearErrorInfo 清除触发元素所对应的错误信息显示元素的错误信息]
 * @param  {[jquery]} event [事件对象]
 */
function clearErrorInfo(event) {
	getErrorElement($(event.target)).html("");
}

/**
 * [numberCheck 校验是否是数字]
 * @param  {[dom]} input [触发事件的dom元素]
 * @return {[boolean]}       [校验通过返回true]
 */
function numberCheck(input) {
	var $input = $(input);
	var inputValue = $input.val().trim();
	var $error = getErrorElement($input);
	if(inputValue == "") {
		$error.html("此字段不能为空");
		return false;
	}
	if(!inputValue.match(ExamDesign.numberRegExp)) {
		$error.html("格式非法");
		return false;
	}
	$error.html("");
	return true;
}

/**
 * [selectCheck 检查下拉列表是否选中]
 * @param  {[dom]} select   [触发事件的select元素]
 * @param  {[String]} message [如果检查不通过显示在span上的错误消息]
 * @return {[booloean]} 检查通过返回true
 */
function selectCheck(select, message) {
    var $select = $(select);
    if ($select.val() === "0") {
        getErrorElement($select).html(message);
        return false;
    }
    return true;
}


/**
 * [设置当前时间]
 */
function curTime() {
	var ele = document.getElementById("cur_time");
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	ele.innerHTML = year + "年" + month + "月" + day + "日";
}

/**
 * [addSingleQuestion 添加一个单选题]
 * @param  {[Function]} handler [可选，对生成的副本进行处理，主要用于从题库加入题目，这样可以避免多次连续操纵DOM]
 */
function addSingleQuestion(handler) {
	var $cloned = $("#single_question_model").children().clone();
	//重新设置内容
	$cloned.find("td[name=single_index]").html(ExamDesign.singleQuestionIndex);
	$cloned.find("input[type=radio]").attr("name", "single-answer-" + ExamDesign.singleQuestionIndex);
	//设置移除此题监听器
	$cloned.find("button[name=remove-single-btn]").click(removeSingleQuestion);
	if (handler && typeof handler === "function") {
		handler.call(this, $cloned);
	}
	$("#single_questions").append($cloned);
	ExamDesign.singleQuestionIndex ++;
	//重建事件监听
	setValidators();
}

/**
 * [removeSingleQuestion 移除一道单选题]
 * @param  {[Event]} event [按钮点击事件]
 */
function removeSingleQuestion(event) {
	var $btn = $(event.target);
	var $currentQuestion = $btn.parents("div[class=single_question]");
	var $tdIndex, $this;
	var index = 0;
	//此题后面的序号减一
	var $afterQuestions = $currentQuestion.nextAll();
	$afterQuestions.each(function() {
		$this = $(this);
		$tdIndex = $this.find("td[name=single_index]");
		index = parseInt($tdIndex.html());
		$tdIndex.html(-- index);
		$this.find("input[type=radio]").attr("name", "single-answer-" + index);
	});
	//从校验器缓存中移除四个class=required的元素
	removeValidatorFromCache($currentQuestion, {
		required: 5,
		number: 1
	});
	//如果此题记录了id(从题库加入的)，那么从ExamDesign.questionCache中移除缓存的id
	var id = $currentQuestion.find("input[name=question-id]").val();
	if (id !== "") {
		var array = ExamDesign.questionCache.singles;
		array.splice($.inArray(id, array), 1);
	}
	$currentQuestion.remove();
	//单选序号减一
	ExamDesign.singleQuestionIndex --;
}

/**
 * [removeValidatorFromCache 从校验器缓存中移除一个题里面的校验器]
 * @param  {[jQuery]} $question [当前应该被移除的题目]
 * @param  {[Object]} config [配置对象，应有required和number两个参数，分别代表两种校验器类型应该移除的个数]
 * @return {[null]}          [无返回值]
 */
function removeValidatorFromCache($question, config) {
	var firstInput = $question.find("input[class=required]").get(0);
	var eles = ExamDesign.validators.requireds.elements;
	eles.splice(eles.indexOf(firstInput), config.required);
	firstInput = $question.find("input[class=number]").get(0);
	eles = ExamDesign.validators.numbers.elements;
	eles.splice(eles.indexOf(firstInput), config.number);
}

/**
 * [removeMultiQuestion 删除一个多选题]
 * @param  {[Event]} event [按钮点击事件]
 */
function removeMultiQuestion(event) {
	var $btn = $(event.target);
	var $currentQuestion = $btn.parent().parent().parent().parent().parent();
	var $tdIndex, $this;
	var index = 0;
	//此题后面的序号减一
	var $afterQuestions = $currentQuestion.nextAll();
	$afterQuestions.each(function() {
		$this = $(this);
		$tdIndex = $this.find("td[name=multi_index]");
		index = parseInt($tdIndex.html());
		$tdIndex.html(-- index);
	});
	//从校验器缓存中移除
	removeValidatorFromCache($currentQuestion, {
		required: 5,
		number: 1
	});
	//如果此题记录了id(从题库加入的)，那么从ExamDesign.questionCache中移除缓存的id
	var id = $currentQuestion.find("input[name=question-id]").val();
	if (id !== "") {
		var array = ExamDesign.questionCache.multis;
		array.splice($.inArray(id, array), 1);
	}
	$currentQuestion.remove();
	//单选序号减一
	ExamDesign.multiQuestionIndex --;
}

/**
 * [addMultiQuestion 添加一个多选题]
 * @param  {[Function]} handler [可选，对生成的副本进行处理，主要用于从题库加入题目，这样可以避免多次连续操纵DOM]
 */
function addMultiQuestion(handler) {
	var $cloned = $("#multi_question_model").children().clone();
	//重新设置内容
	$cloned.find("td[name=multi_index]").html(ExamDesign.multiQuestionIndex);
	//设置移除此题监听器
	$cloned.find("button[name=remove-multi-btn]").click(removeMultiQuestion);
	if (handler && typeof handler === "function") {
		handler.call(this, $cloned);
	}
	$("#multi_questions").append($cloned);
	ExamDesign.multiQuestionIndex ++;
	setValidators();
}

/**
 * [removeJudgeQuestion 删除判断题]
 * @param  {[Event]} event [按钮点击事件]
 */
function removeJudgeQuestion(event) {
	var $btn = $(event.target);
	var $currentQuestion = $btn.parent().parent().parent().parent().parent();
	var $tdIndex, $this;
	var index = 0;
	//此题后面的序号减一
	var $afterQuestions = $currentQuestion.nextAll();
	$afterQuestions.each(function() {
		$this = $(this);
		$tdIndex = $this.find("td[name=judge_index]");
		index = parseInt($tdIndex.html());
		$tdIndex.html(-- index);
		$this.find("input[type=radio]").attr("name", "judge-answer-" + index);
	});
	removeValidatorFromCache($currentQuestion, {
		required: 1,
		number: 1
	});
	//如果此题记录了id(从题库加入的)，那么从ExamDesign.questionCache中移除缓存的id
	var id = $currentQuestion.find("input[name=question-id]").val();
	if (id !== "") {
		var array = ExamDesign.questionCache.judges;
		array.splice($.inArray(id, array), 1);
	}
	$currentQuestion.remove();
	//单选序号减一
	ExamDesign.judgeQuestionIndex --;
}

/**
 * [addJudgeQuestion 增加一道判断题]
 * @param  {[Function]} handler [可选，对生成的副本进行处理，主要用于从题库加入题目，这样可以避免多次连续操纵DOM]
 */
function addJudgeQuestion(handler) {
	var $cloned = $("#judge_question_model").children().clone();
	//重新设置内容
	$cloned.find("td[name=judge_index]").html(ExamDesign.judgeQuestionIndex);
	$cloned.find("input[type=radio]").attr("name", "judge-answer-" + ExamDesign.judgeQuestionIndex);
	//设置移除此题监听器
	$cloned.find("button[name=remove-judge-btn]").click(removeJudgeQuestion);
	if (handler && typeof handler === "function") {
		handler.call(this, $cloned);
	}
	$("#judge_questions").append($cloned);
	ExamDesign.judgeQuestionIndex ++;
	setValidators();
}

/**
 * [toggleRunTime 切换运行时间输入框]
 */
function toggleRunTime(isShow) {
	var $runTime = $("#run-time"),
		input = $runTime.find("input")[0],
		eles;
	if (isShow) {
		//加入对运行时间的校验
		ExamDesign.validators.numbers.elements.push(input);
		$runTime.css("display", "table-row");
	} else {
		//移除对运行时间的校验
		eles = ExamDesign.validators.numbers.elements;
		eles.splice(eles.indexOf(input, 1));
		$runTime.css("display", "none");
	}
}

/**
 * [[切换题面板显示状态]]
 * @param {[[Number]]} type [[题目的类型，其对应关系如下:]]
 * 1 => 单选题
 * 2 => 多选题
 * 3 => 判断题
 * @param {[[Boolean]]} isShow [[显示]]
 */
function toggleBank(type, isShow) {
	var mapper = {
		"1": "SINGLE",
		"2": "MULTI",
		"3": "JUDGE"
	};
	var $bank = $("#question_bank");
	if (isShow) {
		ExamDesign.questionBankType = type;
		$.post("teacher/question/ajax", "type=" + mapper[type], function(data) {
			var $container = $("#bank-container");
			if (data.result === "0") {
				$container.html("加载失败，请稍候再试");
			} else if (data.result === "1") {
				var str = "", e;
				var array = ExamDesign.questionBankType === "1" ? ExamDesign.questionCache.singles :
					(ExamDesign.questionBankType === "2" ? ExamDesign.questionCache.multis : ExamDesign.questionCache.judges);
				for (var i = 0, l = data.data.length;i < l;i ++) {
					e = data.data[i];
					//如果此题已经被添加过，那么不再显示
					if ($.inArray(e.id, array) === -1) {
						//模板示例:
						/*<li optiona="3" optionb="2" optionc="4" optiond="1" point="10" answer="1">
	        				<input type="checkbox">
	        				<span>阿森纳是第几?</span>
	        			</li>*/
						str += "<li  optiona='" + e.optionA + "' optionb='" + e.optionB + "' optionc='" + 
							e.optionC + "' optiond='" + e.optionD + "' point='" + e.point + "' answer='" +
							e.answer + "' id='" + e.id  + "'><input type='checkbox'><span>" + e.title + "</span>";
					}
				}
				$(str).appendTo($container.empty());
			}
		}, "json");
		$bank.css("display", "block");
	} else {
		$bank.css("display", "none");
	}
}

/**
 * [addQuestionFromBank 根据ExamDesign.questionBankType保存的题库题目类型把选择的题目保存到指定的位置]
 */
function addQuestionFromBank() {
	var type = ExamDesign.questionBankType;
	//获取所有被选中的checkbox
	var $boxes = $("#question_bank input:checked");
	//$destination 题目应该加入到的区域，由题型决定
	//$li checkbox所在的li元素
	//answer 答案，如果是单选题，那么只是一个数字，多选题是多个数字，以逗号分割，判断题也是一个数字(0/1)
	var $destination, $li, answer, id;
	if (type === "1") {
        $destination = $("#single_questions");
        $.each($boxes, function (index, input) {
            $li = $(input).parent(), id = $li.attr("id");
            ExamDesign.questionCache.singles.push(id);
            addSingleQuestion(function ($question) {
                $question.find("input[name=single_title]").val($li.find("span").html());
                $question.find("input[name=single_optionA]").val($li.attr("optionA"));
                $question.find("input[name=single_optionB]").val($li.attr("optionB"));
                $question.find("input[name=single_optionC]").val($li.attr("optionC"));
                $question.find("input[name=single_optionD]").val($li.attr("optionD"));
                $question.find("input[name=single_optionD]").val($li.attr("optionD"));
                //设置答案
                answer = $li.attr("answer");
                $question.find("input[type=radio]")[answer].checked = "checked";
                //设置分值
                $question.find("input[name=single_point]").val($li.attr("point"));
                //记录此题的id
                $question.find("input[name=question-id]").val(id);
            });
        });
    } else if (type === "2") {
        $destination = $("#multi_questions");
        $.each($boxes, function (index, input) {
            $li = $(input).parent();
            id = $li.attr("id");
            ExamDesign.questionCache.multis.push(id);
            addMultiQuestion(function ($question) {
                $question.find("input[name=multi_title]").val($li.find("span").html());
                $question.find("input[name=multi_optionA]").val($li.attr("optionA"));
                $question.find("input[name=multi_optionB]").val($li.attr("optionB"));
                $question.find("input[name=multi_optionC]").val($li.attr("optionC"));
                $question.find("input[name=multi_optionD]").val($li.attr("optionD"));
                $question.find("input[name=multi_optionD]").val($li.attr("optionD"));
                //设置分值
                $question.find("input[name=multi_point]").val($li.attr("point"));
                //记录此题的id
                $question.find("input[name=question-id]").val(id);
                //设置答案，此时是一个由逗号分隔的字符串，比如1,2
                answer = $li.attr("answer");
                var array = answer.split(",");
                $.each(array, function (index, a) {
                    $question.find("input[type=checkbox]")[a].checked = "checked";
                });
            });
        });
    } else {
        $destination = $("#judge_questions");
        $.each($boxes, function (index, input) {
            $li = $(input).parent();
            id = $li.attr("id")
            ExamDesign.questionCache.judges.push(id);
            addJudgeQuestion(function ($question) {
                $question.find("input[name=judge_title]").val($li.find("span").html());
                //设置分值
                $question.find("input[name=judge_point]").val($li.attr("point"));
                //设置答案，此时是一个由逗号分隔的字符串，比如1,2
                answer = $li.attr("answer");
                $question.find("input[type=radio]")[answer].checked = "checked";
                //记录此题的id
                $question.find("input[name=question-id]").val(id);
            });
        });
	}
	toggleBank(0, false);
}