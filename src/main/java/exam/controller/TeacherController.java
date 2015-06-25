package exam.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import exam.model.Exam;
import exam.model.page.PageBean;
import exam.service.ExamService;
import exam.util.DataUtil;

/**
 * 教师部分
 * @author skywalker
 *
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {
	
	@Resource
	private ExamService examService;
	@Value("#{properties['exam.pageSize']}")
	private int pageSize;
	@Value("#{properties['exam.pageNumber']}")
	private int pageNumber;

	/**
	 * 转到教师模块主页
	 */
	@RequestMapping("/index")
	public String index() {
		return "teacher/index";
	}
	
	/**
	 * 显示试卷列表
	 * @param pn 页码，输入输入非法，那么为1
	 * @param search 搜索内容，按照标题搜索
	 */
	@RequestMapping("/exam/list")
	public String examList(String pn, String search, Model model) {
		int pageCode = DataUtil.getPageCode(pn);
		String where = "where 1 = 1 ";
		if(DataUtil.isValid(search)) {
			where += " and title like '%" + search + "%'"; 
		}
		PageBean<Exam> pageBean = examService.pageSearch(pageCode, pageSize, pageNumber, where, null, null);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("search", search);
		return "teacher/exam_list";
	}
	
}
