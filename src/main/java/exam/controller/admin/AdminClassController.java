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
import exam.model.Grade;
import exam.model.Major;
import exam.model.page.PageBean;
import exam.service.ClazzService;
import exam.service.GradeService;
import exam.service.MajorService;
import exam.util.DataUtil;
import exam.util.json.JSONObject;

/**
 * 管理员班级管理
 * @author skywalker
 *
 */
@Controller
@RequestMapping("/admin/clazz")
public class AdminClassController {
	
	@Resource
	private ClazzService clazzService;
	@Resource
	private GradeService gradeService;
	@Resource
	private MajorService majorService;
	@Value("#{properties['clazz.pageSize']}")
	private int pageSize;
	@Value("#{properties['clazz.pageNumber']}")
	private int pageNumber;

	/**
	 * 班级列表
	 * @param pn 页码，默认1
	 * @param grade 年级id
	 * @param major 专业id
	 */
	@RequestMapping("/list")
	public String list(String pn, String grade, String major, Model model) {
		int pageCode = DataUtil.getPageCode(pn);
		String where = "where 1 = 1 ";
		List<Object> params = new ArrayList<Object>(2);
		if(DataUtil.isNumber(grade)) {
			where += " and gid = ? ";
			params.add(Integer.parseInt(grade));
		}
		if(DataUtil.isNumber(major)) {
			where += " and mid = ? ";
			params.add(Integer.parseInt(major));
		}
		PageBean<Clazz> pageBean = clazzService.pageSearch(pageCode, pageSize, pageNumber, where, params, null);
		//加载年级和专业列表
		List<Grade> grades = gradeService.findAll();
		List<Major> majors = majorService.findAll();
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("grades", grades);
		model.addAttribute("majors", majors);
		model.addAttribute("grade", grade);
		model.addAttribute("major", major);
		return "admin/clazz_list";
	}
	
	/**
	 * 班级保存
	 * @param grade 年级id
	 * @param major 专业id
	 * @param clazz 班级号
	 */
	@RequestMapping("/add")
	@ResponseBody
	public void add(String grade, String major, String clazz, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if(!DataUtil.isNumber(grade, major, clazz)) {
			json.addElement("result", "0").addElement("message", "格式非法");
		}else {
			int gradeId = Integer.parseInt(grade);
			int majorId = Integer.parseInt(major);
			int cno = Integer.parseInt(clazz);
			if(clazzService.isExist(gradeId, majorId, cno)) {
				json.addElement("result", "0").addElement("message", "此班已存在");
			}else {
				Clazz clazzObj = new Clazz();
				clazzObj.setCno(cno);
				clazzObj.setGrade(new Grade(gradeId));
				clazzObj.setMajor(new Major(majorId));
				clazzService.saveOrUpdate(clazzObj);
				json.addElement("result", "1").addElement("message", "添加成功");
			}
		}
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 班级删除
	 * @param cid 班级id
	 */
	@RequestMapping("/delete/{cid}")
	@ResponseBody
	public void delete(@PathVariable Integer cid, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		clazzService.delete(cid);
		json.addElement("result", "1").addElement("message", "删除成功");
		DataUtil.writeJSON(json, response);
	}
	
}
