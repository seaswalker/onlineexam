package exam.dto;

/**
 * 封装生成成绩报告时一个学生的考试信息
 * @author skywalker
 *
 */
public class StudentReport {

	//试卷标题,这个属性其实只需要一次
	private String title;
	//学生id
	private String sid;
	//姓名
	private String name;
	private int point;
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	
}
