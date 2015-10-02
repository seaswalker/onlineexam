package exam.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.model.Clazz;
import exam.model.page.PageBean;
import exam.model.role.Teacher;
import exam.service.ClazzService;
import exam.service.TeacherService;
import exam.util.DataUtil;
import exam.util.StringUtil;
import exam.util.json.JSONArray;
import exam.util.json.JSONObject;

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
	@Resource
	private ClazzService clazzService;
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
	
	/**
	 * 教师添加
	 * @param id 教职工号
	 * @param name 教师姓名
	 */
	@RequestMapping("/add")
	@ResponseBody
	public void add(String id, String name, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		name = StringUtil.htmlEncode(name);
		if(!DataUtil.isValid(id, name)) {
			json.addElement("result", "0").addElement("message", "格式非法");
		}else if(teacherService.isExist(id)) {
			json.addElement("result", "0").addElement("message", "此教师已存在");
		}else {
			teacherService.saveTeacher(id, name, StringUtil.md5("1234"));
			json.addElement("result", "1").addElement("message", "保存成功");
		}
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 教师编辑
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public void edit(String id, String name, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		name = StringUtil.htmlEncode(name);
		if(!DataUtil.isValid(id, name)) {
			json.addElement("result", "0").addElement("message", "格式非法");
		}else {
			teacherService.updateName(id, name);
			json.addElement("result", "1").addElement("message", "保存成功");
		}
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 以json格式返回此老师所教的班级
	 * @param tid 教师的教职工号
	 */
	@RequestMapping("/clazz/list")
	@ResponseBody
	public void clazz(String tid, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if(!DataUtil.isValid(tid)) {
			json.addElement("result", "0").addElement("message", "数据格式非法");
		}else {
			List<Clazz> clazzs = clazzService.findByTeacher(tid);
			JSONArray array = new JSONArray();
			for(Clazz c : clazzs) {
				array.addObject(c.getJSON());
			}
			json.addElement("result", "1").addElement("data", array);
		}
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 更改教师所教的班级
	 * @param ids 班级id，格式1,2,3
	 */
	@RequestMapping("/clazz/save")
	@ResponseBody
	public void clazzSave(String ids, String tid, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if(!DataUtil.isValid(ids, tid)) {
			json.addElement("result", "0").addElement("message", "格式非法");
		}else {
			teacherService.updateTeachClazzs(ids, tid);
			json.addElement("result", "1").addElement("message", "修改成功");
		}
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 删除教师
	 * @param tid 教师id
	 * @param response
	 */
	@RequestMapping("/delete/{tid}")
	public void delete(@PathVariable String tid, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		teacherService.delete(tid);
		json.addElement("result", "1").addElement("message", "删除成功");
		DataUtil.writeJSON(json, response);
	}
	
}
