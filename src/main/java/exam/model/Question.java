package exam.model;

import java.io.Serializable;

import exam.model.role.Teacher;
import exam.util.json.JSON;
import exam.util.json.JSONAble;
import exam.util.json.JSONObject;

/**
 * 问题
 * @author skywalker
 *
 */
public class Question implements Serializable, JSONAble {

	private static final long serialVersionUID = 3817117285809180416L;
	private static String[] answerFacades = {"A", "B", "C", "D"};
	private static String[] judgeAnserFacades = {"对", "错"};
	
	private int id;
	private String title;
	private String optionA;
	private String optionB;
	private String optionC;
	private String optionD;
	private String answer;
	//答案存储的是序号，比如1，但是显示在页面上的应该是字母的选项或是对错的形式
	private String answerFacade;
	private QuestionType type;
	private int point;
	private Teacher teacher;
	
	/**
	 * 返回此题的门面答案
	 */
	public String getAnswerFacade() {
		return this.answerFacade;
	}
	
	@Override
	public JSON getJSON() {
		JSONObject json = new JSONObject();
		json.addElement("id", String.valueOf(id)).addElement("title", title).addElement("optionA", optionA)
			.addElement("optionB", optionB).addElement("optionC", optionC).addElement("optionD", optionD)
			.addElement("answer", answer).addElement("point", String.valueOf(point));
		return json;
	}
	
	@Override
	public String toString() {
		return "Question [id=" + id + ", title=" + title + ", optionA="
				+ optionA + ", optionB=" + optionB + ", optionC=" + optionC
				+ ", optionD=" + optionD + ", answer=" + answer + ", type="
				+ type + ", point=" + point + "]";
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOptionA() {
		return optionA;
	}
	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}
	public String getOptionB() {
		return optionB;
	}
	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}
	public String getOptionC() {
		return optionC;
	}
	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}
	public String getOptionD() {
		return optionD;
	}
	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}
	public String getAnswer() {
		return answer;
	}
	/**
	 * TODO 此方法需要先设置题型，这是一个隐藏的bug?
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
		//设置其门面
		if (this.type == QuestionType.SINGLE) {
			this.answerFacade = answerFacades[Integer.parseInt(answer)];
		} else if (this.type == QuestionType.MULTI) {
			String[] answers = this.answer.split(",");
			StringBuilder facade = new StringBuilder();
			for (String a : answers) {
				facade.append(answerFacades[Integer.parseInt(a)]).append(",");
			}
			this.answerFacade = facade.deleteCharAt(facade.length() - 1).toString();
		} else {
			this.answerFacade = judgeAnserFacades[Integer.parseInt(answer)];
		}
	}
	public QuestionType getType() {
		return type;
	}
	public void setType(QuestionType type) {
		this.type = type;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
}
