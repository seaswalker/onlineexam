package exam.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import exam.dto.MarkedQuestion;

/**
 * 一次考试批阅的结果
 * @author skywalker
 *
 */
public class ExaminationResult {

	private int id;
	private int examId;
	//考试的标题(多加了一个字段，这样就不用多表查询了)
	private String examTitle;
	private String studentId;
	//考试分数
	private int point;
	//考试时间
	private Date time;
	
	private List<MarkedQuestion> markedQuestions = new ArrayList<MarkedQuestion>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getExamId() {
		return examId;
	}
	public void setExamId(int examId) {
		this.examId = examId;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getExamTitle() {
		return examTitle;
	}
	public void setExamTitle(String examTitle) {
		this.examTitle = examTitle;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	public List<MarkedQuestion> getMarkedQuestions() {
		return Collections.unmodifiableList(markedQuestions);
	}
	
	public void addMarkedQuestion(MarkedQuestion question) {
		this.markedQuestions.add(question);
	}
	
}
