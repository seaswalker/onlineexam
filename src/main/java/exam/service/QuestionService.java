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
	
	/**
	 * 根据试题查找与之相关联的试题
	 * 此方法不用find实现而单独做一个接口是因为试卷和题目的关联是通过一张表建立的，所以试题对象并不能获取试卷的信息
	 * @param eid 试卷id
	 * @return
	 */
	public List<Question> findByExam(int eid);
	
}
