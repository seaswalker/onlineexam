package exam.model;

import java.io.Serializable;

/**
 * 班级
 * @author skywalker
 *
 */
public class Clazz implements Serializable {

	private static final long serialVersionUID = 3193377452706700152L;
	
	private int id;
	//班级序号，比如二班就是2
	private int cno;
	private Major major;
	private Grade grade;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCno() {
		return cno;
	}
	public void setCno(int cno) {
		this.cno = cno;
	}
	public Major getMajor() {
		return major;
	}
	public void setMajor(Major major) {
		this.major = major;
	}
	public Grade getGrade() {
		return grade;
	}
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	
}
