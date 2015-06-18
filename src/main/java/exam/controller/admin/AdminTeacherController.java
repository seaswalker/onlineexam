package exam.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import exam.model.page.PageBean;
import exam.model.role.Teacher;
import exam.service.TeacherService;
import exam.util.DataUtil;

/**
 * 管理员老师管理
 * @author skywalker
 *
 */
@Controller
@RequestMapping("/admin/teacher")
public class AdminTeacherController {
	
	@Resource
	private TeacherService teacherService;
	@Value("#{properties['teacher.pageSize']}")
	private int pageSize;
	@Value("#{properties['teacher.pageNumber']}")
	private int pageNumber;

	/**
	 * 老师列表
	 * @param id 教职工号
	 * @param search 搜索内容
	 */
	@RequestMapping("/list")
	public String list(String pn, String id, String name, Model model) {
		int pageCode = DataUtil.getPageCode(pn);
		String where = " where 1 = 1 ";
		List<Object> params = new ArrayList<Object>(1);
		//教职工编号可以有字母, 如果教职工号不为空，那么不需要模糊搜索
		if(DataUtil.isValid(id)) {
			where += "and id = ?";
			params.add(id);
		}else if(DataUtil.isValid(name)) {
			where += "and name like '%" + name + "%'";
		}
		PageBean<Teacher> pageBean = teacherService.pageSearch(pageCode, pageSize, pageNumber, where, params, null);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("name", name);
		return "admin/teacher_list";
	}
	
}
