package exam.controller.student;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.dto.ExaminationAnswer;
import exam.model.Exam;
import exam.model.ExaminationResult;
import exam.model.page.PageBean;
import exam.model.role.Student;
import exam.service.ExamService;
import exam.service.ExaminationResultService;
import exam.util.DataUtil;
import exam.util.json.JSONObject;

/**
 * 学生模块下试卷操作
 * @author skywalker
 *
 */
@Controller("exam.controller.student.ExamController")
@RequestMapping("/student/exam")
public class ExamController {
	
	@Resource
	private ExamService examService;
	@Resource
	private ExaminationResultService examinationResultService;
	@Value("#{properties['student.exam.pageSize']}")
	private int pageSize;
	@Value("#{properties['student.exam.pageNumber']}")
	private int pageNumber;

	@RequestMapping("/list")
	public String listHelper(Model model, HttpServletRequest request) {
		return list("1", model, request);
	}

	/**
	 * 参加考试，返回所有可用的(适用学生所在的班级)的试题
	 */
	@RequestMapping("/list/{pn}")
	public String list(@PathVariable String pn, Model model, HttpServletRequest request) {
		Student student = (Student) request.getSession().getAttribute("student");
		int pageCode = DataUtil.getPageCode(pn);
		String where = "where id in (select eid from exam_class where cid = (select cid from student where id = '"
    			+ student.getId() + "'))";
		PageBean<Exam> pageBean = examService.pageSearch(pageCode, pageSize, pageNumber, where, null, null);
		model.addAttribute("pageBean", pageBean);
		return "student/exam_list";
	}
	
	/**
	 * 学生参加考试，返回指定的试题
	 * @param eid 试题id
	 * @param model
	 * @return
	 */
	@RequestMapping("/{eid}")
	public String take(@PathVariable Integer eid, Model model, HttpServletRequest request) {
		//检查该学生是否已经参加过此次考试
		HttpSession session = request.getSession();
		String sid = ((Student) session.getAttribute("student")).getId();
		if (examService.hasJoined(eid, sid)) {
			model.addAttribute("message", "您已参加过此次考试");
			return "error";
		}
		Exam exam = new Exam();
		exam.setId(eid);
		Exam result = examService.findWithQuestions(exam);
		if (result == null) {
			return "error";
		}
		model.addAttribute("exam", result);
		model.addAttribute("eid", eid);
		//把题目缓存进入session，这样可以避免批卷时再次访问数据库
		request.getSession().setAttribute("exam", result);
		return "student/exam_take";
	}
	
	/**
	 * 考试结果保存
	 * @param result 结果json串
	 * @param model
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public void submit(String result, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (!DataUtil.isValid(result)) {
			json.addElement("result", "0");
		} else {
			//解析为ExaminationResult
			ExaminationAnswer ea = DataUtil.parseAnswers(result);
			//批卷
			HttpSession session = request.getSession();
			Exam exam = (Exam) session.getAttribute("exam");
			String studentId = ((Student) session.getAttribute("student")).getId();
			ExaminationResult er = DataUtil.markExam(ea, exam, studentId);
			examinationResultService.saveOrUpdate(er);
			session.removeAttribute("exam");
			json.addElement("result", "1").addElement("point", String.valueOf(er.getPoint()));
		}
		DataUtil.writeJSON(json, response);
	}
	
}
