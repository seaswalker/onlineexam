package exam.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.QuestionDao;
import exam.dao.base.BaseDao;
import exam.model.Question;
import exam.model.QuestionType;
import exam.service.QuestionService;
import exam.service.base.BaseServiceImpl;

@Service("questionService")
public class QuestionServiceImpl extends BaseServiceImpl<Question> implements QuestionService {
	
	private QuestionDao questionDao;

	@Resource(name = "questionDao")
	@Override
	protected void setBaseDao(BaseDao<Question> baseDao) {
		super.baseDao = baseDao;
		this.questionDao = (QuestionDao) baseDao;
	}

	@Override
	public List<Question> getSingles(String tid) {
		return getQuestionsByType(tid, QuestionType.SINGLE);
	}

	@Override
	public List<Question> getMultis(String tid) {
		return getQuestionsByType(tid, QuestionType.MULTI);
	}

	@Override
	public List<Question> getJudges(String tid) {
		return getQuestionsByType(tid, QuestionType.JUDGE);
	}
	
	@Override
	public void update(Question entity) {
		String sql = "update question set title = ?, optionA = ?, optionB = ?, optionC = ?, optionD = ?" +
				", answer = ?, point = ? where id = ?";
		questionDao.executeSql(sql, new Object[] {entity.getTitle(), entity.getOptionA(), entity.getOptionB(),
				entity.getOptionC(), entity.getOptionD(), entity.getAnswer(), entity.getPoint(), entity.getId()});
	}
	
	@Override
	public boolean isUsedByExam(int id) {
		String sql = "select count(id) from exam_question where qid = " + id;
		BigInteger count = (BigInteger) questionDao.queryForObject(sql, BigInteger.class);
		return count.intValue() > 0;
	}
	
	@Override
	public void delete(Object id) {
		// TODO 暂不实现，因为涉及到了答案表
	}
	
	/**
	 * 内部使用，根据题目类型和教师获取所有题目
	 * @param tid 教师id
	 * @param type 题型
	 */
	private List<Question> getQuestionsByType(String tid, QuestionType type) {
		String sql = questionDao.getSql() + " where tid = '" + tid + "' and type = '" + type.name() + "'";
		return questionDao.queryBySQL(sql);
	}

}
