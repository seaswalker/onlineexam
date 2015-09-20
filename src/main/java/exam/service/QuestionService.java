package exam.service;

import java.util.List;

import exam.model.Question;
import exam.service.base.BaseService;

public interface QuestionService extends BaseService<Question> {

	/**
	 * 获取教师的全部单选题
	 * @param tid 教师id
	 */
	List<Question> getSingles(String tid);
	
	/**
	 * 获取教师的全部多选题
	 * @param tid 教师id
	 */
	List<Question> getMultis(String tid);
	
	/**
	 * 获取教师的全部判断题
	 * @param tid 教师id
	 */
	List<Question> getJudges(String tid);
	
	/**
	 * 检查题目是否被试卷使用
	 * @param id 题目id
	 */
	boolean isUsedByExam(int id);
	
}
