package exam.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.model.Grade;
import exam.model.page.PageBean;
import exam.service.GradeService;
import exam.util.DataUtil;
import exam.util.json.JSON;
import exam.util.json.JSONObject;

/**
 * 后台年级管理
 * @author skywalker
 *
 */
@Controller
@RequestMapping("/admin/grade")
public class AdminGradeController {

	@Resource
	private GradeService gradeService;
	@Value("#{properties['grade.pageSize']}")
	private int pageSize;
	//显示的页码数量
	@Value("#{properties['grade.pageNumber']}")
	private int pageNumber;
	
	/**
	 * 显示年级列表
	 */
	@RequestMapping("/list")
	public String list(String pn, String search, Model model) {
		int pageCode = DataUtil.getPageCode(pn);
		String where = null;
		if(DataUtil.isNumber(search)) {
			where = " where grade = " + search;
		}
		PageBean<Grade> pageBean = gradeService.pageSearch(pageCode, pageSize, pageNumber, where, null, null);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("search", search);
		return "admin/grade_list";
	}
	
	/**
	 * 添加年级
	 */
	@RequestMapping("/add")
	@ResponseBody
	public void add(String grade, HttpServletResponse response) {
		JSON json = new JSONObject();
		if(!DataUtil.isNumber(grade)) {
			json.addElement("result", "0").addElement("message", "请输入数字，比如2012");
		}else if(gradeService.findByGrade(grade) != null) {
			json.addElement("result", "0").addElement("message", "此年级已存在");
		}else {
			gradeService.saveOrUpdate(new Grade(0, Integer.parseInt(grade)));			
			json.addElement("result", "1").addElement("message", "年级添加成功");
		}
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 删除年级
	 * @param gid 年级id
	 */
	@RequestMapping("/delete/{gid}")
	@ResponseBody
	public void delete(@PathVariable Integer gid, HttpServletResponse response) {
		JSON json = new JSONObject();
		gradeService.delete(gid);
		json.addElement("result", "1").addElement("message", "删除成功");
		DataUtil.writeJSON(json, response);
	}
	
}
