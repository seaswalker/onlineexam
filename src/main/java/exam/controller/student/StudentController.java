package exam.controller.student;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 学生模块学生操作
 * @author skywalker
 *
 */
@Controller("exam.controller.student.StudentController")
@RequestMapping("/student")
public class StudentController {
	
	/**
	 * 学生主页
	 */
	@RequestMapping("/index")
	public String index() {
		return "student/index";
	}
	
}
