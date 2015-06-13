package exam.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 管理员主页
 * @author skywalker
 *
 */
@Controller
public class AdminIndexController {

	/**
	 * 转向主页
	 */
	@RequestMapping("/admin/index")
	public String index() {
		return "admin/index";
	}
	
}
