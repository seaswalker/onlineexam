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
@Controller("exam.controller.teacher.ExamController")
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
		Exam result = DataUtil.parseExam(exam, teacher);
		//总分为零，说明试卷为空，不允许
		if (result.getPoints() == 0) {
			json.addElement("result", "0").addElement("message", "请不要提交空试卷!");
		} else {
			examService.saveOrUpdate(result);
			json.addElement("result", "1");
		}
		DataUtil.writeJSON(json, response);
	}

    /**
     * 删除一套试题
     * @param examId 试卷id
     * @param response
     */
    @RequestMapping("/remove")
    @ResponseBody
    public void delete(Integer examId, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        if (!DataUtil.isValid(examId)) {
            json.addElement("result", "0");
        } else {
            examService.delete(examId);
            json.addElement("result", "1");
        }
        DataUtil.writeJSON(json, response);
    }

    /**
     * 切换试卷的状态
     * @param examId 试卷id
     * @param days 运行的天数，此参数仅在切换至正正在运行(RUNNING)状态时才有效
     * @param status 要切换到的状态
     */
    @RequestMapping("/status")
    @ResponseBody
    public void switchStatus(Integer examId, String status, Integer days, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        if (!DataUtil.isValid(examId) || !DataUtil.isValid(status)) {
            json.addElement("result", "0");
        } else {
            examService.switchStatus(examId, status, days);
            json.addElement("result", "1");
        }
        DataUtil.writeJSON(json, response);
    }

}
