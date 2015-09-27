package exam.dto;

import exam.util.json.JSON;
import exam.util.json.JSONAble;
import exam.util.json.JSONObject;

/**
 * 封装班级、年级、专业
 * @author skywalker
 *
 */
public class ClassDTO implements JSONAble {

	//年级id
	private int gid;
	//年级
	private int grade;
	//专业id
	private int mid;
	//专业
	private String major;
	//班级id
	private int cid;
	//班级
	private int cno;
	
	@Override
	public JSON getJSON() {
		JSONObject json = new JSONObject();
		json.addElement("gid", String.valueOf(gid)).addElement("grade", String.valueOf(grade))
			.addElement("mid", String.valueOf(mid)).addElement("major", major)
			.addElement("cid", String.valueOf(cid)).addElement("cno", String.valueOf(cno));
		return json;
	}
	
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getMid() {
		return mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getCno() {
		return cno;
	}
	public void setCno(int cno) {
		this.cno = cno;
	}
	
}
