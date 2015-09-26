package exam.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.model.role.Manager;
import exam.model.role.Student;
import exam.model.role.Teacher;
import exam.service.ManagerService;
import exam.service.StudentService;
import exam.service.TeacherService;
import exam.session.SessionContainer;
import exam.util.DataUtil;
import exam.util.StringUtil;
import exam.util.json.JSON;
import exam.util.json.JSONObject;

/**
 * 用户登录
 * @author skywalker
 *
 */
@Controller
public class LoginController {
	
	@Resource
	private ManagerService managerService;
	@Resource
	private TeacherService teacherService;
	@Resource
	private StudentService studentService;

	/**
	 * 转到登录页面
	 */
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	
	/**
	 * 真正的登录
	 * @param role 1-->> 学生 2-->> 教师 3--> 管理员
	 */
	@RequestMapping("/login/do")
	public String doLogin(String username, String password, String verify, int role, Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (!DataUtil.isValid(username, password) || !DataUtil.checkVerify(verify, session)) {
			return "error";
		}
		if (role == 3) {
			Manager manager = managerService.login(username, StringUtil.md5(password));
			if (manager == null) {
				model.addAttribute("error", "用户名或密码错误");
				return "login";
			}
			manager.setPassword(password);
			//管理员账户已在别处登录，强迫之前登录的立即下线
			if (SessionContainer.adminSession != null) {
				SessionContainer.adminSession.setAttribute("force", Boolean.TRUE);
			}
			SessionContainer.adminSession = session;
			session.setAttribute("admin", manager);
			return "redirect:/admin/index";
		} else if (role == 2) {
			Teacher teacher = teacherService.login(username, password);
			if (teacher == null) {
				model.addAttribute("error", "用户名或密码错误");
				return "login";
			}
			teacher.setPassword(password);
			//如果此账户已在别处登录
			if (SessionContainer.loginTeachers.containsKey(teacher.getId())) {
				SessionContainer.loginTeachers.get(teacher.getId()).setAttribute("force", Boolean.TRUE);
			}
			SessionContainer.loginTeachers.put(teacher.getId(), session);
			session.setAttribute("teacher", teacher);
			return "redirect:/teacher/index";
		} else if (role == 1) {
			Student student = studentService.login(username, password);
			if (student == null) {
				model.addAttribute("error", "用户名或密码错误");
				return "login";
			}
			student.setPassword(password);
			//检测是否在别处登录
			if (SessionContainer.loginStudents.containsKey(student.getId())) {
				SessionContainer.loginStudents.get(student.getId()).setAttribute("force", Boolean.TRUE);
			}
			SessionContainer.loginStudents.put(student.getId(), session);
			session.setAttribute("student", student);
			return "redirect:/student/index";
		}
		return "";
	}
	
	/**
	 * ajax请求检查验证码
	 * @param verify
	 * @param response
	 */
	@RequestMapping("/login/verify")
	@ResponseBody
	public void rand(String verify, HttpServletResponse response, HttpSession session) {
		JSON json = new JSONObject();
		if(DataUtil.checkVerify(verify, session)) {
			json.addElement("result", "1");
		}else {
			json.addElement("result", "0");
		}
		DataUtil.writeJSON(json, response);
	}
	
}
