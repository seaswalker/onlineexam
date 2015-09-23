//获取考试统计
$(function() {
	$.post("teacher/exam/statistics/do/" + $("#eid").val(), null, function(data) {
		
	}, "json");
});