	/**
	 * 弹出提示、错误、成功窗
	 */
	 var Tips = {
	 	isInit: false,
		_init: function() {
			var css = document.createElement("link");
			css.rel = "stylesheet";
			css.href = "css/tips.css";
			var head = document.getElementsByTagName("head")[0];
			head.appendChild(css);
			Tips.isInit = true;
		},
		showMessage: function(message) {
			Tips._show_window_div(message, "message_window");
		},
		showError: function(message) {
			Tips._show_window_div(message, "error_window");
		},
		showSuccess: function(message) {
			Tips._show_window_div(message, "success_window");
		},
		_show_window_div: function(message, class_name) {
			if(!Tips.isInit) {
				Tips._init();
			}
			var window_div = Tips._get_window_div();
			window_div.innerHTML = message;
			//动态根据文本长度计算信息条宽度
			var width = 300 / 12 * message.length;
			window_div.style.width = width + "px";
			window_div.style.marginLeft = "-" + width / 2 + "px";
			window_div.className = class_name;
			window_div.style.display = "block";
			Tips._close_window(window_div, 3);
		},
		_get_window_div: function() {
			var wd = document.getElementById("window_div");
			var window_div;
			if(wd != null && wd != undefined) {
				window_div = wd;
			}else {
				window_div = document.createElement("div");
				window_div.id = "window_div";
				document.body.appendChild(window_div);
			}
			return window_div;
		},
		_close_window: function(object, seconds) {
			setTimeout(
				function(){
					object.style.display = "none";
				},
				seconds * 1000
			);
		}
	 }

	 function test() {
	 }