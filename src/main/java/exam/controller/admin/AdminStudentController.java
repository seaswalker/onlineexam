package exam.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.model.Clazz;
import exam.model.page.PageBean;
import exam.model.role.Student;
import exam.service.StudentService;
import exam.util.DataUtil;
import exam.util.StringUtil;
import exam.util.json.JSON;
import exam.util.json.JSONObject;

/**
 * 后台学生管理
 * @author skywalker
 *
 */
@Controller
@RequestMapping("/admin/student")
public class AdminStudentController {
	
	@Resource
	private StudentService studentService;
	@Value("#{properties['student.pageSize']}")
	private int pageSize;
	@Value("#{properties['student.pageNumber']}")
	private int pageNumber;

	@RequestMapping("/list")
	public String list(String pn, String search, Model model) {
		int pageCode = DataUtil.getPageCode(pn);
		String where = null;
		if(DataUtil.isValid(search)) {
			where = " where s.name like '%" + search + "%'";
		}
		PageBean<Student> pageBean = studentService.pageSearch(pageCode, pageSize, pageNumber, where, null, null);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("search", search);
		return "admin/student_list";
	}
	
	/**
	 * 添加学生
	 * @param id 学号，不可重复
	 * @param name 学生姓名
	 * @param clazz 班级id
	 */
	@RequestMapping("/add")
	@ResponseBody
	public void add(String id, String clazz, String name, HttpServletResponse response) {
		JSON json = new JSONObject();
		if(!DataUtil.isNumber(id, clazz)) {
			json.addElement("result", "0").addElement("message", "数据格式非法");
		}else if(!DataUtil.isValid(name)) {
			json.addElement("result", "0").addElement("message", "请输入学生姓名");
		}else {
			Student student = new Student();
			student.setId(id);
			student.setName(name);
			//默认密码1234
			student.setPassword(StringUtil.md5("1234"));
			student.setClazz(new Clazz(Integer.parseInt(clazz)));
			studentService.save(student);
			json.addElement("result", "1").addElement("message", "学生添加成功");
		}
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 检测学号是否存在
	 * @param id 学号
	 * @param response
	 */
	@RequestMapping("/check")
	@ResponseBody
	public void check(String id, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if(!DataUtil.isNumber(id)) {
			json.addElement("result", "0").addElement("message", "格式非法");
		}else if(studentService.isExisted(id)) {
			json.addElement("result", "1").addElement("exist", "1");
		}else {
			json.addElement("result", "1").addElement("exist", "0");
		}
		DataUtil.writeJSON(json, response);
	}
	
}
