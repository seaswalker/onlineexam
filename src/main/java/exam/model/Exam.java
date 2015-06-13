package exam.model;

import java.io.Serializable;
import java.util.Date;

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
	//时间限制，单位分钟，默认60
	private int limit = 60;
	private Date beginTime;
	private Date endTime;
	private boolean close;
	//总分
	private int points;
	private int singlePoints;
	private int multiPoints;
	private int judgePoints;
	private int shortPoints;
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
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public boolean isClose() {
		return close;
	}
	public void setClose(boolean close) {
		this.close = close;
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
	public int getShortPoints() {
		return shortPoints;
	}
	public void setShortPoints(int shortPoints) {
		this.shortPoints = shortPoints;
	}
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
}
