package exam.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import exam.dao.ExaminationResultDao;
import exam.dao.base.BaseDao;
import exam.dao.base.GenerateKeyCallback;
import exam.dto.MarkedQuestion;
import exam.model.ExaminationResult;
import exam.service.ExaminationResultService;
import exam.service.base.BaseServiceImpl;

@Service("examinationResultService")
public class ExaminationResultServiceImpl extends BaseServiceImpl<ExaminationResult> implements ExaminationResultService {

	private ExaminationResultDao examinationResultDao;
	
	@Resource(name = "examinationResultDao")
	@Override
	protected void setBaseDao(BaseDao<ExaminationResult> baseDao) {
		super.baseDao = baseDao;
		this.examinationResultDao = (ExaminationResultDao) baseDao;
	}
	
	/**
	 * 更新功能没有实现,因为考试结果不可修改
	 */
	@Override
	public void saveOrUpdate(ExaminationResult entity) {
		//首先保存ExaminationResult
		String sql = "insert into examinationresult values(null, ?, ?, ?, ?, ?)";
		int erId = examinationResultDao.getKeyHelper(sql, new GenerateKeyCallback() {
			@Override
			public void setParameters(PreparedStatement ps, Object param) throws SQLException {
				ExaminationResult er = (ExaminationResult) param;
				ps.setInt(1, er.getExamId());
				ps.setString(2, er.getStudentId());
				ps.setInt(3, er.getPoint());
				ps.setTimestamp(4, new Timestamp(er.getTime().getTime()));
				ps.setString(5, er.getExamTitle());
			}
		}, entity);
		//然后保存各个题目
		StringBuilder sb = new StringBuilder("insert into examinationresult_question values");
		for (MarkedQuestion mq : entity.getMarkedQuestions()) {
			sb.append("(null,").append(erId).append(",").append(mq.getQuestionId()).append(",")
				.append(mq.isRight()).append(",'").append(mq.getWrongAnswer()).append("'),");
		}
		examinationResultDao.executeSql(sb.deleteCharAt(sb.length() - 1).toString());
	}

}
