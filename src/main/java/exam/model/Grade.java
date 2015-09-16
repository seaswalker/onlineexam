package exam.model;

import java.io.Serializable;

import exam.util.json.JSON;
import exam.util.json.JSONAble;
import exam.util.json.JSONObject;

/**
 * 年级
 * @author skywalker
 *
 */
public class Grade implements Serializable, JSONAble {

	private static final long serialVersionUID = -3270807327813730922L;
	
	private int id;
	//年级，比如2012
	private int grade;
	
	public Grade(int id) {
		this.id = id;
	}
	
	public Grade() {}
	
	public Grade(int id, int grade) {
		this.id = id;
		this.grade = grade;
	}
	
	/**
	 * 返回此对象对应的json对象
	 */
	public JSON getJSON() {
		JSONObject object = new JSONObject();
		object.addElement("id", String.valueOf(id))
			.addElement("grade", String.valueOf(grade));
		return object;
	}
	
	@Override
	public String toString() {
		return "Grade [id=" + id + ", grade=" + grade + "]";
	}

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
