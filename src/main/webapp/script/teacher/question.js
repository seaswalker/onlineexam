$(function() {
	QuestionHelper.initListeners();
	QuestionHelper.initValidators();
	QuestionHelper.questionType = $("#question-type").val();
	QuestionHelper.isSingle = (QuestionHelper.questionType === "SINGLE");
	QuestionHelper.isMulti = (QuestionHelper.questionType === "MULTI");
	QuestionHelper.isJudge = (QuestionHelper.questionType === "JUDGE");
});

var QuestionHelper = {
	//题型,可取的值:SINGLE MULTI JUDGE
	questionType: null,
	//是添加还是编辑，默认编辑
	isEdit: true,
	//缓存当前正在操作的question
	current: null,
	//记录题目是否真正的被修改了
	modified: false,
	isSingle: true,
	isMulti: false,
	isJudge: false,
	Question: {
		//答案映射会数字(序号)
		answerMapper: {
			A: "0",
			B: "1",
			C: "2",
			D: "3"
		},
		judgeMapper: {
			"对": "0",
			"错": "1"
		},
		//创建一个Question对象
		//$tr -> 触发showEdit函数的button所在的tr，以此可以获取所有的题目信息
		create: function($tr) {
			var question = {};
			var $tds = $tr.children("td");
			question.id = $tds[0].innerHTML;
			question.title = $tds[1].innerHTML;
			if (QuestionHelper.isJudge) {
				question.answer = this.judgeMapper[$tds[2].innerHTML];
				question.point = $tds[3].innerHTML;
			} else {
				question.optionA = $tds[2].innerHTML;
				question.optionB = $tds[3].innerHTML;
				question.optionC = $tds[4].innerHTML;
				question.optionD = $tds[5].innerHTML;
				if (QuestionHelper.isSingle) {
					question.answer = this.answerMapper[$tds[6].innerHTML];
				} else {
					//B,C
					var str = $tds[6].innerHTML;
					var arr = [], as = str.split(",");
					for (var i = 0, l = as.length;i < l;i ++) {
						arr.push(this.answerMapper[as[i]]);
					}
					question.answer = arr.join(",");
				}
				question.point = $tds[7].innerHTML;
			}
			return question;
		}
	},
	//加载事件监听器
	initListeners: function() {
		$("#question-list button[name=show-edit-btn]").click(function() {
			QuestionHelper.showEdit(this);
		});
		//关闭题目编辑
		$("#close-edit-btn").click(QuestionHelper.closeEdit);
		//题目修改保存
		$("#question-save-btn").click(QuestionHelper.saveQuestion);
		//删除按钮
		$("#question-list button[name=delete-btn]").click(function() {
			QuestionHelper.deleteQuestion(this);
		});
		//添加题目
		$("#add-question-btn").click(QuestionHelper.showAdd);
		//显示正答率
		$("#question-list button[name=show-rate-btn]").click(function() {
			QuestionHelper.showRate(this);
		});
		//关闭正答率
		$("#close-rate-btn").click(QuestionHelper.closeRate);
	},
	//加载校验器
	initValidators: function() {
		//加载非空校验
		var $questionEdit = $("#question-edit"), array;
		array = QuestionHelper.Validator.cache.requires;
		$questionEdit.find("input[class=required]").each(function() {
			var error = _getError(this);
			//加入校验器缓存
			array.push({
				input: this,
				error: error
			});
			$(this).blur(function() {
				QuestionHelper.Validator.notNullValidator(this, error);
			}).focus(function() {
				error.innerHTML = "";
			});
		});	
		array = QuestionHelper.Validator.cache.numbers;
		//加载数字校验
		$questionEdit.find("input[class=number]").each(function() {
			var error = _getError(this);
			//加入校验器缓存
			array.push({
				input: this,
				error: error
			});
			$(this).blur(function() {
				QuestionHelper.Validator.numberValidator(this, error);
			}).focus(function() {
				error.innerHTML = "";
			});
		});	
		//获取input元素对应的错误信息显示span，结构为:
		//<td><input type="text" name="optionA" class="required" /></td>
		//<td><span class="error"></span></td>
		function _getError(input) {
			return $(input).parent().next().find("span")[0];
		}
		
	},
	//校验器
	Validator: {
		//正整数正则
		numberPattern: /^[1-9][0-9]*$/,
		//缓存需要执行校验的元素，以便最后保存问题时触发，数组里面元素的格式示例： {input, error}
		cache: {
			requires: [],
			numbers: []
		},
		//非空校验器
		//input 需要校验的目标元素 error: 显示错误信息的元素
		notNullValidator: function(input, error) {
			var value = $.trim(input.value);
			if (value === "") {
				error.innerHTML = "请输入内容";
				return false;
			}
			//判断是否和原值一样
			if (QuestionHelper.isEdit && !QuestionHelper.modified && value !== QuestionHelper.current[input.name]) {
				QuestionHelper.modified = true;
			}
			return value;
		},
		numberValidator: function(input, error) {
			var value;
			if (!(value = QuestionHelper.Validator.notNullValidator(input, error))) {
				return false;
			}
			if (!value.match(QuestionHelper.Validator.numberPattern)) {
				error.innerHTML = "格式非法";
				return false;
			}
			//判断是否和原值一样
			if (QuestionHelper.isEdit && !QuestionHelper.modified && value !== QuestionHelper.current[input.name]) {
				QuestionHelper.modified = true;
			}
			return value;
		}
	},
	//显示题目编辑界面
	//button 触发事件的按钮
	showEdit: function(button) {
		var $questionEdit = $("#question-edit");
		var $tr = $(button).parents("tr");
		//设置面板里面各个input的值
		var question = QuestionHelper.current = this.Question.create($tr);
		var $inputs = $questionEdit.find("input[type=text]");
		$inputs[0].value = question.title;
		if (QuestionHelper.isJudge) {
			$inputs[1].value = question.point;
			$questionEdit.find("input[type=radio]")[question.answer].checked = "checked";
		} else {
			$inputs[1].value = question.optionA;
			$inputs[2].value = question.optionB;
			$inputs[3].value = question.optionC;
			$inputs[4].value = question.optionD;
			$inputs[5].value = question.point;
			if (QuestionHelper.isSingle) {
				$questionEdit.find("input[type=radio]")[question.answer].checked = "checked";
			} else {
				var $boxes = $questionEdit.find("input[type=checkbox]");
				var as = question.answer.split(",");
				for (var i = 0, l = as.length;i < l;i ++) {
					$boxes[as[i]].checked = "checked";
				}
			}
		}
		$questionEdit.show();
	},
	//关闭题目编辑
	closeEdit: function() {
		var $questionEdit = $("#question-edit");
		$questionEdit.hide();
		//关闭时清空各个错误信息显示框
		var array = QuestionHelper.Validator.cache.requires, e
		for (var i = 0, l = array.length;i < l;i ++) {
			e = array[i];
			e.error.innerHTML = "";
			e.input.value = "";
		}
		array = QuestionHelper.Validator.cache.numbers;
		for (i = 0, l = array.length;i < l;i ++) {
			e = array[i];
			e.error.innerHTML = "";
			e.input.value = "";
		}
		QuestionHelper.isEdit = true;
	},
	saveQuestion: function() {
		//支持单选和多选
		var answer, i, l,
			$checkeds = $("#question-answer-container input:checked");
		if (QuestionHelper.questionType === "MULTI") {
			if ($checkeds.length < 2) {
				$("#question-error").html("请选择至少两个答案");
				return;
			}
			//拼接答案串
			var array = [];
			for (i = 0, l = $checkeds.length;i < l;i ++) {
				array.push($checkeds[i].value);
			}
			answer = array.join(",");
		} else {
			answer = $checkeds[0].value;
		}
		//如果有内容做出了修改，执行下面的步骤才是有意义的
		if (!QuestionHelper.isEdit || QuestionHelper.modified || answer !== QuestionHelper.current.answer) {
			//触发所有校验
			var requires = QuestionHelper.Validator.cache.requires,
				requireValidator = QuestionHelper.Validator.notNullValidator;
			//values用来记录各个input个值，避免再次从dom获取
			//values里面值得顺序是:title-optionA-optionB-optionC-optionD-point
			//答案需要再次访问dom
			var e, value, values = [];
			for (var i = 0, l = requires.length;i < l;i ++) {
				e = requires[i];
				if (!(value = requireValidator.call(QuestionHelper.Validator, e.input, e.error))) {
					return;
				}
				values.push(value);
			}
			var numbers = QuestionHelper.Validator.cache.numbers,
				numberValidator = QuestionHelper.Validator.numberValidator;
			for (i = 0, l = numbers.length;i < l;i ++) {
				e = numbers[i];
				if (!(value = numberValidator.call(QuestionHelper.Validator, e.input, e.error))) {
					return;
				}
				values.push(value);
			}
			var data;
			if (QuestionHelper.isJudge) {
				data = "id=" + (QuestionHelper.current ? QuestionHelper.current.id : "-1") + "&title=" + values[0] + 
					"&point=" + values[1] + "&answer=" + answer + "&type=" + QuestionHelper.questionType;
			} else {
				//参数串
				data = "id=" + (QuestionHelper.current ? QuestionHelper.current.id : "-1") + "&title=" + values[0] + "&optionA=" + values[1] +
					"&optionB=" + values[2] + "&optionC=" + values[3] + "&optionD=" + values[4] + "&point=" + values[5]
					+ "&answer=" + answer + "&type=" + QuestionHelper.questionType;
			}
			//提交
			$.post("teacher/question/save", data, function(data) {
				if (data.result === "0") {
					Tips.showError("保存失败，请稍候再试");
				} else if (data.result === "1") {
					Tips.showSuccess("保存成功");
					setTimeout(function() {
						window.location.reload();
					}, 3000);
				}
			}, "json");
		}
	},
	deleteQuestion: function(button) {
		var id = $(button).parents("tr").find("td:first").html();
		if (confirm("您确认删除此题?")) {
			$.ajax({
				url: "teacher/question/delete/" + id,
				dataType: "json",
				method: "post",
				async: false,
				success: function(data) {
					if (data.result === "0") {
						Tips.showError(data.message);
					} else if (data.result === "1") {
						Tips.showSuccess(data.message);
						setTimeout(function() {
							window.location.reload();
						}, 3000);
					}
				}
			});
		}
	},
	//显示题目添加
	showAdd: function() {
		$("#question-edit").show();
		QuestionHelper.isEdit = false;
	},
	//显示正答率
	showRate: function(button) {
		var $rateWindow = $("#rate-window"), $div = $("#rate");
		$div.html("正在加载...");
		$rateWindow.show();
		var id = $(button).parents("tr").find("td:first").html();
		//发送ajax请求
		$.post("teacher/question/rate/" + id, null, function(data) {
			if (data.result === "0") {
				Tips.showError(data.message);
			} else if (data.result === "1") {
				$div.html(data.rate);
			}
		}, "json");
	},
	//关闭正答率窗体
	closeRate: function() {
		$("#rate-window").hide();
	}
};