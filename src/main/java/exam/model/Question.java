package exam.model;

import java.io.Serializable;

import exam.model.role.Teacher;

/**
 * 问题
 * @author skywalker
 *
 */
public class Question implements Serializable {

	private static final long serialVersionUID = 3817117285809180416L;
	
	private int id;
	private String title;
	private String optionA;
	private String optionB;
	private String optionC;
	private String optionD;
	private String answer;
	/**
	 * 问题类型
	 * 1单选
	 * 2多选
	 * 3判断
	 * 4简单
	 */
	private int type;
	private int point;
	private Teacher teacher;
	
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
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
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
