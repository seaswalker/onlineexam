/**
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
	}
};

$(function() {
	curTime();
	setListeners();
	//设置输入校验
	setValidators();
});

/**
 * [setValidators 设置输入校验]
 */
function setValidators() {
	//设置非空校验器
	var requireds = 
		$("#single_questions input[class=required], #multi_questions input[class=required], #judge_questions input[class=required]").toArray(),
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
    });
    $gradeSelect.focus(clearErrorInfo);
    var $majorSelect = $("#major_select");
    $majorSelect.blur(function(event) {
    	selectCheck.call(this, event.target, "请选择专业");
    });
    $majorSelect.focus(clearErrorInfo);
    var $clazzSelect = $("#clazz_select");
    $clazzSelect.blur(function(event) {
    	selectCheck.call(this, event.target, "请选择班级");
    });
    $clazzSelect.focus(clearErrorInfo);
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
    		},
    		{
    			element: $clazzSelect[0],
    			message: "请选择班级"
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
				title: "XXX",
				optionA: "XXX",
				optionB: "XXX",
				optionC: "XXX",
				optionD: "XXX",
				answer: "XXX",
				point: 10
			}
		],
		multis: [
			格式同上
		],
		judges: [
			{
				title: "XXX",
				answer: "XXX",
				point: 10
			}
		],
		setting: {
			timeLimit: 10,
			grade: 2012,
			major: "土木",
			clazz: 2,
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
		question.title = $container.find("input[name=" + typeStr + "_title]").val();
		question.point = $container.find("input[name=" + typeStr + "_point]").val();
		//判断题不需要选项
		if (type < 3) {
			question.optionA = $container.find("input[name=" + typeStr + "_optionA]").val();
			question.optionB = $container.find("input[name=" + typeStr + "_optionB]").val();
			question.optionC = $container.find("input[name=" + typeStr + "_optionC]").val();
			question.optionD = $container.find("input[name=" + typeStr + "_optionD]").val();
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
	var $otherSetting = $("#other-setting");
	result.setting.timeLimit = $otherSetting.find("input[name=time_limit]").val();
	result.setting.grade = $("#grade_select").val();
	result.setting.major = $("#major_select").val();
	result.setting.clazz = $("#clazz_select").val();
	if ($otherSetting.find("input:checked").val() === "1") {
		result.setting.status = 1;
		result.setting.runTime = $otherSetting.find("input[name=run_time]").val();
	} else {
		result.setting.status = 0;
	}
	$.post("teacher/exam/save", {json: JSON.stringify(result)}, function(data) {
		if (data.result === "1") {
			Tips.showSuccess("添加成功");
			setTimeout(function() {
				window.location.href = "teacher/index";
			}, 3000);
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
	var $currentQuestion = $btn.parent().parent().parent().parent().parent();
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
	var $bank = $("#question_bank");
	if (isShow) {
		ExamDesign.questionBankType = type;
		//TODO 使用ajax加载该老师的特定题型的所有题目
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
	var $destination, $li, answer;
	if (type === "1") {
        $destination = $("#single_questions");
        $.each($boxes, function (index, input) {
            $li = $(input).parent();
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
            });
        });
    } else if (type === "2") {
        $destination = $("#multi_questions");
        $.each($boxes, function (index, input) {
            $li = $(input).parent();
            addMultiQuestion(function ($question) {
                $question.find("input[name=multi_title]").val($li.find("span").html());
                $question.find("input[name=multi_optionA]").val($li.attr("optionA"));
                $question.find("input[name=multi_optionB]").val($li.attr("optionB"));
                $question.find("input[name=multi_optionC]").val($li.attr("optionC"));
                $question.find("input[name=multi_optionD]").val($li.attr("optionD"));
                $question.find("input[name=multi_optionD]").val($li.attr("optionD"));
                //设置分值
                $question.find("input[name=multi_point]").val($li.attr("point"));
                //设置答案，此时是一个由逗号分隔的字符串，比如1,2
                answer = $li.attr("answer");
                var array = answer.split(",");
                $.each(array, function (index, a) {
                    $question.find("input[type=radio]")[a].checked = "checked";
                });
            });
        });
    } else {
        $destination = $("#judge_questions");
        $.each($boxes, function (index, input) {
            $li = $(input).parent();
            addJudgeQuestion(function ($question) {
                $question.find("input[name=judge_title]").val($li.find("span").html());
                //设置分值
                $question.find("input[name=judge_point]").val($li.attr("point"));
                //设置答案，此时是一个由逗号分隔的字符串，比如1,2
                answer = $li.attr("answer");
                $question.find("input[type=radio]")[answer].checked = "checked";
            });
        });
	}
	toggleBank(0, false);
}