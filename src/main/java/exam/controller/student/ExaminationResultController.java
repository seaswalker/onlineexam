package exam.controller.student;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import exam.dto.ERView;
import exam.model.ExaminationResult;
import exam.model.page.PageBean;
import exam.model.role.Student;
import exam.service.ExaminationResultService;
import exam.util.DataUtil;

@Controller("exam.controller.student.ExaminationResultController")
@RequestMapping("/student/notes")
public class ExaminationResultController {
	
	@Resource
	private ExaminationResultService examinationResultService;
	@Value("#{properties['student.examinationResult.pageSize']}")
	private int pageSize;
	@Value("#{properties['student.examinationResult.pageNumber']}")
	private int pageNumber;
	
	@RequestMapping
	public String notesHelper(HttpServletRequest request, Model model) {
		return notes("1", request, model);
	}

	/**
	 * 转向考试记录页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/{pn}")
	public String notes(@PathVariable String pn, HttpServletRequest request, Model model) {
		Student student = (Student) request.getSession().getAttribute("student");
		int pageCode = DataUtil.getPageCode(pn);
		String where = " where sid = '" + student.getId() + "' ";
		PageBean<ExaminationResult> pageBean = examinationResultService.pageSearch(pageCode, pageSize, pageNumber, where, null, null);
		model.addAttribute("pageBean", pageBean);
		return "student/examinationResult_list";
	}
	
	/**
	 * 浏览考试详细信息
	 * @param id 考试结果(ExaminationResult)的id
	 * @param model
	 * @return
	 */
	@RequestMapping("/view/{id}")
	public String view(@PathVariable Integer id, Model model) {
		if (!DataUtil.isValid(id)) {
			return "error";
		}
		ERView view = examinationResultService.getViewById(id);
		model.addAttribute("view", view);
		return "student/examinationResult_view";
	}
	
}
