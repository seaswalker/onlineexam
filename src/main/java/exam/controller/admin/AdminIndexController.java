package exam.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.model.role.Manager;
import exam.service.ManagerService;
import exam.util.DataUtil;
import exam.util.json.JSONObject;

/**
 * 管理员主页
 * @author skywalker
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminIndexController {
	
	@Resource
	private ManagerService managerService;

	/**
	 * 转向主页
	 */
	@RequestMapping("/index")
	public String index() {
		return "admin/index";
	}
	
	/**
	 * 转向密码修改页面
	 * @return
	 */
	@RequestMapping("/password")
	public String password() {
		return "admin/password";
	}
	
	/**
     * 校验旧密码
     * @param password 旧密码
     */
    @RequestMapping("/password/check")
    @ResponseBody
    public void check(String password, HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        Manager manager = (Manager) request.getSession().getAttribute("admin");
        if (manager.getPassword().equals(password)) {
            json.addElement("result", "1");
        } else {
            json.addElement("result", "0");
        }
        DataUtil.writeJSON(json, response);
    }
	
	@RequestMapping("/password/modify")
    public String modifyPassword(String oldPassword, String newPassword, HttpServletRequest request, Model model) {
        Manager manager = (Manager) request.getSession().getAttribute("admin");
        if (!checkPassword(oldPassword, newPassword, manager)) {
            return "error";
        }
        managerService.updatePassword(manager.getId(), newPassword);
        manager.setPassword(newPassword);
        manager.setModified(true);
        model.addAttribute("message", "密码修改成功");
        model.addAttribute("url", request.getContextPath() + "/admin/index");
        return "success";
    }

    /**
     * 检查旧密码和新密码
     * @param oldPassword 必须和session里面保存的密码一致
     * @param newPassword 必须是4-10，由数字、字母、下划线组成
     * @param teacher
     * @return 通过返回true
     */
    private boolean checkPassword(String oldPassword, String newPassword, Manager manager) {
        if (!manager.getPassword().equals(oldPassword)) {
            return false;
        }
        if (!DataUtil.isValid(newPassword) || !newPassword.matches("^\\w{4,10}$")) {
            return false;
        }
        return true;
    }
	
}
