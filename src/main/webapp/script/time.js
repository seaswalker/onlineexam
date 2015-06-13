/**
 * 显示当前时间
 */
window.onload = function() {
	_cur_time();
};

/**
 * [设置当前时间]
 */
function _cur_time() {
	var ele = document.getElementById("cur_time");
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	ele.innerHTML = year + "年" + month + "月" + day + "日";
}