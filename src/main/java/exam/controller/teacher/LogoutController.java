package exam.controller.teacher;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 教师退出
 * 
 * @author skywalker
 *
 */
@Controller("exam.controller.teacher")
public class LogoutController {

	@RequestMapping("/teacher/logout")
	public String logout(HttpServletRequest request) {
		// 清除session中teacher
		request.getSession().removeAttribute("teacher");
		return "redirect:/login";
	}

}
