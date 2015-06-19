package exam.controller.admin;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
		session.removeAttribute("admin");
		return "login";
	}
	
}
