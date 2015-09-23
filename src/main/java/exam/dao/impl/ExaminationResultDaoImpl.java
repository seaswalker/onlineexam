package exam.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import exam.dao.ExaminationResultDao;
import exam.dao.base.BaseDaoImpl;
import exam.model.ExaminationResult;

@Repository("examinationResultDao")
public class ExaminationResultDaoImpl extends BaseDaoImpl<ExaminationResult> implements ExaminationResultDao {

	private static String sql = "select * from examinationresult";
	private static String countSql = "select count(id) from examinationresult";
	private static RowMapper<ExaminationResult> rowMapper;
	
	static {
		rowMapper = new RowMapper<ExaminationResult>() {
			@Override
			public ExaminationResult mapRow(ResultSet rs, int rowNum) throws SQLException {
				ExaminationResult er = new ExaminationResult();
				er.setId(rs.getInt("id"));
				er.setPoint(rs.getInt("point"));
				er.setExamId(rs.getInt("eid"));
				er.setExamTitle(rs.getString("examtitle"));
				er.setStudentId(rs.getString("sid"));
				er.setTime(rs.getTimestamp("time"));
				return er;
			}
		};
	}
	
	@Override
	public <T> T query(String sql, ResultSetExtractor<T> resultSetExtractor) {
		return jdbcTemplate.query(sql, resultSetExtractor);
	}
	
	@Override
	public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	@Override
	public String getCountSql() {
		return countSql;
	}

	@Override
	public String getSql() {
		return sql;
	}

	@Override
	public RowMapper<ExaminationResult> getRowMapper() {
		return rowMapper;
	}

}
