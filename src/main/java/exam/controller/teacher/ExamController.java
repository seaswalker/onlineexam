package exam.controller.teacher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.model.Exam;
import exam.model.page.PageBean;
import exam.model.role.Teacher;
import exam.service.ExamService;
import exam.util.DataUtil;
import exam.util.json.JSON;
import exam.util.json.JSONObject;

/**
 * 教师角色-试卷相关控制
 * @author skywalker
 *
 */
@Controller
@RequestMapping("/teacher/exam")
public class ExamController {
	
	@Resource
	private ExamService examService;
	@Value("#{properties['exam.pageSize']}")
	private int pageSize;
	@Value("#{properties['exam.pageNumber']}")
	private int pageNumber;

	/**
	 * 返回试卷列表
	 * @param pn 页码，输入输入非法，那么为1
	 * @param search 搜索内容，按照标题搜索
	 */
	@RequestMapping("/list")
	public String list(String pn, String search, Model model) {
		int pageCode = DataUtil.getPageCode(pn);
		String where = "where 1 = 1 ";
		if (DataUtil.isValid(search)) {
			where += " and title like '%" + search + "%'"; 
		}
		PageBean<Exam> pageBean = examService.pageSearch(pageCode, pageSize, pageNumber, where, null, null);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("search", search);
		return "teacher/exam_list";
	}
	
	/**
	 * 转向试卷添加页面
	 */
	@RequestMapping("/add")
	public String addUI() {
		return "teacher/exam_add";
	}
	
	/**
	 * 添加一套试卷
	 * @param exam 包含所有题目以及设置信息的json字符串
	 */
	@RequestMapping("/save")
	@ResponseBody
	public void add(String exam, HttpServletRequest request, HttpServletResponse response) {
		Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
		JSON json = new JSONObject();
		if (teacher == null) {
			json.addElement("result", "0").addElement("url", "teacher/index");
		} else {
			Exam result = DataUtil.parseExam(exam, teacher);
			examService.save(result);
			json.addElement("result", "1").addElement("url", "teacher/exam/list");
		}
		DataUtil.writeJSON(json, response);
	}
	
}
