package exam.dto;

/**
 * 封装批阅完成的题目
 * @author skywalker
 *
 */
public class MarkedQuestion {

	private int id;
	private int examinationResultId;
	private int questionId;
	private boolean right;
	private String wrongAnswer;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getExaminationResultId() {
		return examinationResultId;
	}
	public void setExaminationResultId(int examinationResultId) {
		this.examinationResultId = examinationResultId;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
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
	}
	
}
