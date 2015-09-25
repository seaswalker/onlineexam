//获取考试统计
$(function() {
	$.post("teacher/exam/statistics/do/" + $("#eid").val(), null, function(data) {
		if (data.result === "0") {
			//没有人参加此考试
			$("#exam-title").html("没有考试记录!");
			$("#wait").hide();
			$("#pie").hide();
			$("#charts").show();
		} else if (data.result === "1") {
			//设置各部分值
			$("#pie").find("img").attr("src", data.url);
			$("#highest-point").html(data.highestPoint);
			$("#lowest-point").html(data.lowestPoint);
			$("#exam-title").html(data.title + "考试统计结果(" + data.count + "人):");
			//最高分和最低分的人
			var array = data.highestNames;
			var str = "";
			for (var i = 0, l = array.length;i < l;i ++) {
				str += array[i].name + "&nbsp;";
			}
			$("#highest-names").html(str);
			array = data.lowestNames;
			str = "";
			for (i = 0, l = array.length;i < l;i ++) {
				str += array[i].name + "&nbsp;";
			}
			$("#lowest-names").html(str);
			$("#wait").hide();
			$("#charts").show();
		}
	}, "json");
});