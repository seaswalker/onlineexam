package exam.controller.teacher;

import exam.model.role.Teacher;
import exam.service.TeacherService;
import exam.util.DataUtil;
import exam.util.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 教师部分
 * @author skywalker
 *
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Resource
    private TeacherService teacherService;
	
	/**
	 * 转到教师模块主页
	 */
	@RequestMapping("/index")
	public String index() {
		return "teacher/index";
	}

    /**
     * 转向修改密码
     */
    @RequestMapping("/password")
    public String password() {
        return "teacher/password";
    }

    /**
     * 校验旧密码
     * @param password 旧密码
     */
    @RequestMapping("/password/check")
    @ResponseBody
    public void check(String password, HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
        if (teacher.getPassword().equals(password)) {
            json.addElement("result", "1");
        } else {
            json.addElement("result", "0");
        }
        DataUtil.writeJSON(json, response);
    }

    @RequestMapping("/password/modify")
    public String modifyPassword(String oldPassword, String newPassword, HttpServletRequest request, Model model) {
        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
        if (!checkPassword(oldPassword, newPassword, teacher)) {
            return "error";
        }
        teacherService.modifyPassword(teacher.getId(), newPassword);
        teacher.setPassword(newPassword);
        model.addAttribute("message", "密码修改成功");
        model.addAttribute("url", request.getContextPath() + "/teacher/index");
        return "success";
    }

    /**
     * 检查旧密码和新密码
     * @param oldPassword 必须和session里面保存的密码一致
     * @param newPassword 必须是4-10，由数字、字母、下划线组成
     * @param teacher
     * @return 通过返回true
     */
    private boolean checkPassword(String oldPassword, String newPassword, Teacher teacher) {
        if (!teacher.getPassword().equals(oldPassword)) {
            return false;
        }
        if (!DataUtil.isValid(newPassword) || !newPassword.matches("^\\w{4,10}$")) {
            return false;
        }
        return true;
    }
	
}
