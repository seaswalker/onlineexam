package exam.controller.student;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.model.role.Student;
import exam.service.StudentService;
import exam.util.DataUtil;
import exam.util.json.JSONObject;

/**
 * 学生模块学生操作
 * @author skywalker
 *
 */
@Controller("exam.controller.student.StudentController")
@RequestMapping("/student")
public class StudentController {
	
	@Resource
	private StudentService studentService;
	
	/**
	 * 学生主页
	 */
	@RequestMapping("/index")
	public String index() {
		return "student/index";
	}
	
	/**
	 * 转向修改密码界面
	 */
	@RequestMapping("/password")
	public String password() {
		return "student/password";
	}
	
	/**
     * 校验旧密码
     * @param password 旧密码
     */
    @RequestMapping("/password/check")
    @ResponseBody
    public void check(String password, HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        Student student = (Student) request.getSession().getAttribute("student");
        if (student.getPassword().equals(password)) {
            json.addElement("result", "1");
        } else {
            json.addElement("result", "0");
        }
        DataUtil.writeJSON(json, response);
    }
    
    @RequestMapping("/password/modify")
    public String modifyPassword(String oldPassword, String newPassword, HttpServletRequest request, Model model) {
        Student student = (Student) request.getSession().getAttribute("student");
        if (!checkPassword(oldPassword, newPassword, student)) {
            return "error";
        }
        studentService.modifyPassword(student.getId(), newPassword);
        student.setPassword(newPassword);
        model.addAttribute("message", "密码修改成功");
        model.addAttribute("url", request.getContextPath() + "/student/index");
        return "success";
    }
    
    /**
     * 检查旧密码和新密码
     * @param oldPassword 必须和session里面保存的密码一致
     * @param newPassword 必须是4-10，由数字、字母、下划线组成
     * @param teacher
     * @return 通过返回true
     */
    private boolean checkPassword(String oldPassword, String newPassword, Student student) {
        if (!student.getPassword().equals(oldPassword)) {
            return false;
        }
        if (!DataUtil.isValid(newPassword) || !newPassword.matches("^\\w{4,10}$")) {
            return false;
        }
        return true;
    }
	
	
}
