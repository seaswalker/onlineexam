package exam.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import exam.model.role.Teacher;

/**
 * 一套试题
 * @author skywalker
 *
 */
public class Exam implements Serializable {

	private static final long serialVersionUID = -5053744055036244916L;
	
	private int id;
	private String title;
	//时间限制，单位分钟
	private int limit;
	//试卷的截止时间，客户端传来的实际上是运行时间(runTime)，此字段由此计算而来
	private Date endTime;
	//试卷的运行状态，true代表正在运行
	private boolean status;
	//总分,应在解析json字符串是计算而来
	private int points;
	private int singlePoints;
	private int multiPoints;
	private int judgePoints;
	//除此套题的老师
	private Teacher teacher;
	//单选题
	private List<Question> singleQuestions = new ArrayList<Question>();
	//多选题
	private List<Question> multiQuestions = new ArrayList<Question>();
	//判断题
	private List<Question> judgeQuestions = new ArrayList<Question>();
	
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
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getSinglePoints() {
		return singlePoints;
	}
	public void setSinglePoints(int singlePoints) {
		this.singlePoints = singlePoints;
	}
	public int getMultiPoints() {
		return multiPoints;
	}
	public void setMultiPoints(int multiPoints) {
		this.multiPoints = multiPoints;
	}
	public int getJudgePoints() {
		return judgePoints;
	}
	public void setJudgePoints(int judgePoints) {
		this.judgePoints = judgePoints;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	/**
	 * 添加一个单选题
	 */
	public void addSingleQuestion(Question question) {
		if (question == null || question.getType() != QuestionType.SINGLE) {
			return;
		}
		this.singleQuestions.add(question);
	}
	
	public List<Question> getSingleQuestions() {
		return Collections.unmodifiableList(singleQuestions);
	}
	
	/**
	 * 添加多选题
	 */
	public void addMultiQuestion(Question question) {
		if (question == null || question.getType() != QuestionType.MULTI) {
			return;
		}
		this.multiQuestions.add(question);
	}
	
	public List<Question> getMultiQuestions() {
		return Collections.unmodifiableList(multiQuestions);
	}
	
	/**
	 * 添加判断题
	 */
	public void addJudgeQuestion(Question question) {
		if (question == null || question.getType() != QuestionType.JUDGE) {
			return;
		}
		this.judgeQuestions.add(question);
	}
	
	public List<Question> getJudgeQuestions() {
		return Collections.unmodifiableList(judgeQuestions);
	}
	
}
