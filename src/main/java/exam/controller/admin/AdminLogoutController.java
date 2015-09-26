package exam.controller.admin;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import exam.session.SessionContainer;

/**
 * 管理员退出
 * @author skywalker
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminLogoutController {

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		//SessionContainer设为未登录
		SessionContainer.adminSession = null;
		session.invalidate();
		return "login";
	}
	
}
