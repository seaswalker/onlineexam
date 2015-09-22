package exam.controller.teacher;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import exam.model.Question;
import exam.model.QuestionType;
import exam.model.page.PageBean;
import exam.model.role.Teacher;
import exam.service.QuestionService;
import exam.util.DataUtil;
import exam.util.json.JSONArray;
import exam.util.json.JSONObject;

/***
 * 教师部分的题目操作
 * @author skywalker
 *
 */
@Controller("exam.controller.teacher.QuestionController")
@RequestMapping("/teacher/question")
public class QuestionController {
	
	@Resource
	private QuestionService questionService;
	@Value("#{properties['question.pageSize']}")
	private int pageSize;
	@Value("#{properties['question.pageNumber']}")
	private int pageNumber;
	
	/**
	 * TODO 目的是让url teacher/question/singles可以起到teacher/question/singles/1的效果，但是事实上
	 * \@RequestMapping\({"/singles", "/singles/{pn}")这种写法根本不起作用，暂时不知道怎么优雅的解决，先用笨方法代替
	 * @param request
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/singles")
	public String singlesHelper(HttpServletRequest request, Model model) throws IOException {
		return singles("1", null, request, model);
	}
	
	/**
	 * 返回所有的单选题
	 */
	@RequestMapping("/singles/{pn}")
	public String singles(@PathVariable String pn, String search, HttpServletRequest request, Model model) {
		return questions(DataUtil.getPageCode(pn), search, QuestionType.SINGLE, request, model);
	}
	
	/**
	 * 多选查询辅助
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/multis")
	public String multisHelper(HttpServletRequest request, Model model) {
		return multis("1", null, request, model);
	}
	
	/**
	 * 分页查询多选题
	 * @param pn 页码
	 * @param search 搜索内容
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/multis/{pn}")
	public String multis(@PathVariable String pn, String search, HttpServletRequest request, Model model) {
		return questions(DataUtil.getPageCode(pn), search, QuestionType.MULTI, request, model);
	}
	
	/**
	 * 判断查询辅助
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/judges")
	public String judgesHelper(HttpServletRequest request, Model model) {
		return judges("1", null, request, model);
	}
	
	/**
	 * 分页查询判断题
	 * @param pn 页码
	 * @param search 搜索内容
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/judges/{pn}")
	public String judges(@PathVariable String pn, String search, HttpServletRequest request, Model model) {
		return questions(DataUtil.getPageCode(pn), search, QuestionType.JUDGE, request, model);
	}
	
	/**
	 * 添加和修改
	 * @param id 如果id为负值，那么即为添加，反之修改
	 * @param title 
	 * @param optionA
	 * @param optionB
	 * @param optionC
	 * @param optionD
	 * @param answer
	 * @param point
	 * @param type
	 * @param request
	 * @param response
	 */
	@RequestMapping("/save")
	@ResponseBody
	public void save(Integer id, String title, String optionA, String optionB, String optionC, String optionD,
			String answer, Integer point, String type, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (!DataUtil.isValid(point) || !DataUtil.isValid(title, answer, type)
				|| (QuestionType.valueOf(type) != QuestionType.JUDGE && !DataUtil.isValid(optionA, 
						optionB, optionC, optionD))) {
			json.addElement("result", "0");
		} else {
			Question question = new Question();
			question.setType(QuestionType.valueOf(type));
			question.setAnswer(answer);
			question.setId(id);
			question.setOptionA(optionA);
			question.setOptionB(optionB);
			question.setOptionC(optionC);
			question.setOptionD(optionD);
			question.setPoint(point);
			question.setTitle(title);
			question.setTeacher((Teacher) request.getSession().getAttribute("teacher"));
			questionService.saveOrUpdate(question);
			json.addElement("result", "1");
		}
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 删除题目
	 * @param id 题目id
	 */
	@RequestMapping("/delete/{id}")
	@ResponseBody
	public void delete(@PathVariable("id") Integer id, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		//首先检查此题是否被试卷引用
		if (questionService.isUsedByExam(id)) {
			json.addElement("result", "0").addElement("message", "此题目被试卷引用，无法删除");
		} else {
			questionService.delete(id);
			json.addElement("result", "1").addElement("message", "删除成功");
		}
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 反应当前教师的指定题型的所有题目
	 * @param type 题型: SINGLE/MULTI/JUDGE
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ajax")
	@ResponseBody
	public void list(String type, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (!DataUtil.isValid(type)) {
			json.addElement("result", "0");
		} else {
			QuestionType qt = QuestionType.valueOf(type);
			String tid = ((Teacher) request.getSession().getAttribute("teacher")).getId(); 
			List<Question> questions = qt == QuestionType.SINGLE ? questionService.getSingles(tid) :
				(qt == QuestionType.MULTI ? questionService.getMultis(tid) : questionService.getJudges(tid));
			JSONArray array = new JSONArray();
			for (Question q : questions) {
				array.addObject(q.getJSON());
			}
			json.addElement("result", "1").addElement("data", array);
		}
System.out.println(json.toString());
		DataUtil.writeJSON(json, response);
	}
	
	/**
	 * 分页查出各种题型，此方法是singles/multis/judges的真正实现
	 * @param pn 页码
	 * @param search 搜索内容
	 * @param request 
	 * @param model
	 * @return 转向的地址
	 */
	private String questions(int pageCode, String search, QuestionType type, HttpServletRequest request, Model model) {
		Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
		String where = "where tid = '" + teacher.getId() + "' and type = '" + type.name() + "'";
		if (DataUtil.isValid(search)) {
			where += " and title like '%" + search + "%'"; 
		}
		PageBean<Question> pageBean = questionService.pageSearch(pageCode, pageSize, pageNumber, where, null, null);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("search", search);
		model.addAttribute("type", type.name());
		return "teacher/question_list";
	}
	
}
