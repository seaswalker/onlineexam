package exam.controller.teacher;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 教师部分
 * @author skywalker
 *
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {
	
	/**
	 * 转到教师模块主页
	 */
	@RequestMapping("/index")
	public String index() {
		return "teacher/index";
	}
	
}
