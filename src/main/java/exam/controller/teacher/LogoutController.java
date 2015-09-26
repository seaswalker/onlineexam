package exam.controller.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import exam.model.role.Teacher;
import exam.session.SessionContainer;

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
		HttpSession session = request.getSession();
		Teacher teacher = (Teacher) session.getAttribute("teacher");
		SessionContainer.loginTeachers.remove(teacher.getId());
		// 清除session中teacher
		session.invalidate();
		return "redirect:/login";
	}

}
