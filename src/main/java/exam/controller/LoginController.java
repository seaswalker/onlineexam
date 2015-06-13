package exam.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.model.role.Manager;
import exam.service.ManagerService;
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
	public String doLogin(String username, String password, String verify, int role, Model model, HttpSession session) {
		if(!DataUtil.isValid(username, password) || !DataUtil.checkVerify(verify, session)) {
			return "error";
		}
		if(role == 3) {
			Manager manager = managerService.login(username, StringUtil.md5(password));
			if(manager == null) {
				model.addAttribute("error", "用户名或密码错误");
				return "login";
			}
			manager.setPassword(password);
			session.setAttribute("manager", manager);
			return "redirect:/admin/index";
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
