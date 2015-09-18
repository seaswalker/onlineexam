//试卷列表操作
$(function() {
    ExamList.initListeners();
});

var ExamList = {
	//绑定exam_list.jsp中的事件监听函数
	initListeners: function() {
		//显示所选班级
		$("#show-clazz-btn").click(function() {
			$("#class-show").show();
		});
		//移除所选班级
		var that = this;
		$("#remove-class-btn").click(function() {
			that.removeClass();
		});
	},
	//移除所选班级
	removeClass: function() {

	}
};