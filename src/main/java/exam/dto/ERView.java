package exam.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import exam.model.Question;

/**
 * ERView -> ExaminationResultView
 * 封装浏览一次考试结果所需要的全部信息
 * @author skywalker
 *
 */
public class ERView {

	//从examk表获得:
	private int singlePoints;
	private int multiPoints;
	private int judgePoints;
	//从examinationresult获得
	//得分
	private int point;
	//考试时间
	private Date time;
	private List<ERViewQuestion> singleQuestions = new ArrayList<ERView.ERViewQuestion>();
	private List<ERViewQuestion> multiQuestions = new ArrayList<ERView.ERViewQuestion>();
	private List<ERViewQuestion> judgeQuestions = new ArrayList<ERView.ERViewQuestion>();
	
	public List<ERViewQuestion> getsingleQuestions() {
		return Collections.unmodifiableList(singleQuestions);
	}

	public void addSingleQuestion(ERViewQuestion question) {
		this.singleQuestions.add(question);
	}

	public List<ERViewQuestion> getmultiQuestions() {
		return Collections.unmodifiableList(multiQuestions);
	}

	public void addMultiQuestion(ERViewQuestion question) {
		this.multiQuestions.add(question);
	}
	
	public List<ERViewQuestion> getjudgeQuestions() {
		return Collections.unmodifiableList(judgeQuestions);
	}

	public void addJudgeQuestion(ERViewQuestion question) {
		this.judgeQuestions.add(question);
	}
	
	/**
	 * 封装一道题(exam.model.Question)的基本信息、是否做对、错误答案
	 * @author skywalker
	 *
	 */
	public static class ERViewQuestion extends Question {
		
		private static final long serialVersionUID = -5131397582765204363L;
		
		private boolean right;
		private String wrongAnswer;
		//错误答案门面，不能显示数字答案在页面
		private String wrongAnswerFacade;
		
		public boolean isRight() {
			return right;
		}
		public void setRight(boolean right) {
			this.right = right;
		}
		public String getWrongAnswer() {
			return wrongAnswer;
		}
		public void setWrongAnswer(String wrongAnswer) {
			this.wrongAnswer = wrongAnswer;
			//设置门面
			this.wrongAnswerFacade = generateFacade(wrongAnswer);
		}
		public String getWrongAnswerFacade() {
			return wrongAnswerFacade;
		}
		
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
	
}
