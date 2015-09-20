package exam.controller.teacher;

import java.io.IOException;

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
	public String singleHelper(HttpServletRequest request, Model model) throws IOException {
		return single("1", null, request, model);
	}
	
	/**
	 * 返回所有的单选题
	 */
	@RequestMapping("/singles/{pn}")
	public String single(@PathVariable String pn, String search, HttpServletRequest request, Model model) {
		Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
		int pageCode = DataUtil.getPageCode(pn);
		String where = "where tid = '" + teacher.getId() + "' and type = '" + QuestionType.SINGLE.name() + "'";
		if (DataUtil.isValid(search)) {
			where += " and title like '%" + search + "%'"; 
		}
		PageBean<Question> pageBean = questionService.pageSearch(pageCode, pageSize, pageNumber, where, null, null);
		model.addAttribute("pageBean", pageBean);
		model.addAttribute("search", search);
		return "teacher/single_list";
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public void save(Integer id, String title, String optionA, String optionB, String optionC, String optionD,
			String answer, Integer point, String type, HttpServletRequest request, HttpServletResponse response) {
		JSONObject json = new JSONObject();
		if (!DataUtil.isValid(id, point) || !DataUtil.isValid(title, answer, type)
				|| (QuestionType.valueOf(type) != QuestionType.JUDGE && !DataUtil.isValid(optionA, 
						optionB, optionC, optionD))) {
			json.addElement("result", "0");
		} else {
			Question question = new Question();
			question.setAnswer(answer);
			question.setId(id);
			question.setOptionA(optionA);
			question.setOptionB(optionB);
			question.setOptionC(optionC);
			question.setOptionD(optionD);
			question.setPoint(point);
			question.setTitle(title);
			questionService.update(question);
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
	
}
