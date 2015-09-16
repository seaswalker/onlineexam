package exam.model;

/**
 * 题型
 * @author skywalker
 *
 */
public enum QuestionType {

	SINGLE(1),
	MULTI(2),
	JUDGE(3);
	
	private int type;
	
	QuestionType(int type) {
		this.type = type;
	}
	
	public int type() {
		return this.type;
	}
	
}
