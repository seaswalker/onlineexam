package exam.controller.student;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 学生退出
 * @author skywalker
 *
 */
@Controller("exam.controller.student.LogoutController")
public class LogoutController {

	@RequestMapping("/student/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().removeAttribute("student");
		return "redirect:/login";
	}
	
}
