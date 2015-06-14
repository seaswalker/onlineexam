package exam.controller.admin;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
			gradeService.save(new Grade(0, Integer.parseInt(grade)));			
			json.addElement("result", "1").addElement("message", "年级添加成功");
		}
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 修改年级
	 * @param id
	 * @param 年级，应该是数字
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public void edit(String id, String grade, HttpServletResponse response) {
		JSON json = new JSONObject();
		if(!DataUtil.isNumber(id)) {
			json.addElement("result", "0").addElement("message", "非法数据");
		}else if(!DataUtil.isNumber(grade)) {
			json.addElement("result", "0").addElement("message", "请输入数字，比如2012");
		}else {
			int _id = Integer.parseInt(id);
			gradeService.update(new Grade(_id, Integer.parseInt(grade)));
			json.addElement("result", "1").addElement("message", "修改成功");
		}
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 批量删除年级
	 * @param ids 格式为1,2,3
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public void delete(String ids, HttpServletResponse response) {
		JSON json = new JSONObject();
		if(!DataUtil.isValid(ids)) {
			json.addElement("result", "0").addElement("message", "请选择要删除的记录");
		}else {
			gradeService.batchDelete(ids);
			json.addElement("result", "1").addElement("message", "删除成功");
		}
		DataUtil.writeJSON(json, response);
	}
	
}
