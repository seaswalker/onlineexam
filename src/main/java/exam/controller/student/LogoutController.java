package exam.controller.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import exam.model.role.Student;
import exam.session.SessionContainer;

/**
 * 学生退出
 * @author skywalker
 *
 */
@Controller("exam.controller.student.LogoutController")
public class LogoutController {

	@RequestMapping("/student/logout")
	public String logout(HttpServletRequest request) {
		//从SessionContainer中清除登录记录
		HttpSession session = request.getSession();
		Student student = (Student) session.getAttribute("student");
		SessionContainer.loginStudents.remove(student.getId());
		session.invalidate();
		return "redirect:/login";
	}
	
}
