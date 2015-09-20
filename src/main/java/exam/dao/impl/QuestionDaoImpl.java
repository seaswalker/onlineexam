package exam.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import exam.dao.QuestionDao;
import exam.dao.base.BaseDaoImpl;
import exam.model.Question;
import exam.model.QuestionType;

@Repository("questionDao")
public class QuestionDaoImpl extends BaseDaoImpl<Question> implements QuestionDao {
	
	//此处不需要返回关联的教师
	private static String sql = "select * from question";
	private static String countSql = "select count(id) from question";
	private static RowMapper<Question> rowMapper = new RowMapper<Question>() {
		@Override
		public Question mapRow(ResultSet rs, int rowNum) throws SQLException {
			Question question = new Question();
			question.setId(rs.getInt("id"));
			question.setOptionA(rs.getString("optionA"));
			question.setOptionB(rs.getString("optionD"));
			question.setOptionC(rs.getString("optionC"));
			question.setOptionD(rs.getString("optionD"));
			question.setPoint(rs.getInt("point"));
			question.setTitle(rs.getString("title"));
			question.setType(QuestionType.valueOf(rs.getString("type")));
			question.setAnswer(rs.getString("answer"));
			return question;
		}
	};

	@Override
	public String getCountSql() {
		return countSql;
	}

	@Override
	public String getSql() {
		return sql;
	}

	@Override
	public RowMapper<Question> getRowMapper() {
		return rowMapper;
	}

}
