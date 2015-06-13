package exam.model;

import java.io.Serializable;

/**
 * 年级
 * @author skywalker
 *
 */
public class Grade implements Serializable {

	private static final long serialVersionUID = -3270807327813730922L;
	
	private int id;
	//年级，比如2012
	private int grade;
	
	public Grade(int id) {
		this.id = id;
	}
	
	public Grade() {}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	
}
