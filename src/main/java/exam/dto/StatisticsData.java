package exam.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 封装统计考试成绩数据
 * @author skywalker
 *
 */
public class StatisticsData {

	//所统计的试卷的题目
	private String title;
	//参加的人数
	private int personCount;
	//试卷总分
	private int examPoints;
	//在60% 80% 90%节点上具体是多少分，注意此处用了int而不是double
	private int sixtyPoint;
	private int eighttyPoint;
	private int ninetyPoint;
	//最高分学生的姓名及分数
	private List<String> highestNames = new ArrayList<String>();
	private int highestPoint;
	//最低分学生的姓名以及分数
	private List<String> lowestNames = new ArrayList<>();
	private int lowestPoint;
	//总分60%以下(不含60%)
	private List<Integer> underSixty = new ArrayList<>();
	//[60%-80%)
	private List<Integer> sixtyAndEighty = new ArrayList<>();
	//[80%-90%)
	private List<Integer> eightyAndNinety = new ArrayList<>();
	//[90%-100%]
	private List<Integer> aboveNinety = new ArrayList<>();
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPersonCount() {
		return personCount;
	}
	public void setPersonCount(int personCount) {
		this.personCount = personCount;
	}
	public int getSixtyPoint() {
		return sixtyPoint;
	}
	public void setSixtyPoint(int sixtyPoint) {
		this.sixtyPoint = sixtyPoint;
	}
	public int getEighttyPoint() {
		return eighttyPoint;
	}
	public void setEighttyPoint(int eighttyPoint) {
		this.eighttyPoint = eighttyPoint;
	}
	public int getNinetyPoint() {
		return ninetyPoint;
	}
	public void setNinetyPoint(int ninetyPoint) {
		this.ninetyPoint = ninetyPoint;
	}
	public int getExamPoints() {
		return examPoints;
	}
	public void setExamPoints(int examPoints) {
		this.examPoints = examPoints;
	}
	public List<String> getHighestNames() {
		return Collections.unmodifiableList(highestNames);
	}
	
	/**
	 * 添加分数最高的学生姓名
	 */
	public void addHightestName(List<String> names) {
		this.highestNames.addAll(names);
	}
	
	public int getHighestPoint() {
		return highestPoint;
	}
	public void setHighestPoint(int highestPoint) {
		this.highestPoint = highestPoint;
	}
	public List<String> getLowestNames() {
		return Collections.unmodifiableList(lowestNames);
	}
	
	/**
	 * 添加分数最低的学生姓名
	 */
	public void addLowestNames(List<String> names) {
		this.lowestNames.addAll(names);
	}
	
	public int getLowestPoint() {
		return lowestPoint;
	}
	public void setLowestPoint(int lowestPoint) {
		this.lowestPoint = lowestPoint;
	}
	public List<Integer> getUnderSixty() {
		return Collections.unmodifiableList(underSixty);
	}
	
	/**
	 * 添加一个低于60%的分数
	 */
	public void addUnderSixty(int point) {
		this.underSixty.add(point);
	}
	
	public List<Integer> getSixtyAndEighty() {
		return Collections.unmodifiableList(sixtyAndEighty);
	}
	
	/**
	 * 添加一个[60%-80%)之间的分数
	 * @param point
	 */
	public void addSixtyAndEighty(int point) {
		this.sixtyAndEighty.add(point);
	}
	
	public List<Integer> getEightyAndNinety() {
		return Collections.unmodifiableList(eightyAndNinety);
	}
	
	/**
	 * 添加一个[80%-90%)之间的分数
	 * @param point
	 */
	public void addEightyAndNinety(int point) {
		this.eightyAndNinety.add(point);
	}
	
	public List<Integer> getAboveNinety() {
		return Collections.unmodifiableList(aboveNinety);
	}
	
	/**
	 * 添加一个[90%-100%]间的分数
	 * @param point
	 */
	public void addAboveNinety(int point) {
		this.aboveNinety.add(point);
	}
	
}
