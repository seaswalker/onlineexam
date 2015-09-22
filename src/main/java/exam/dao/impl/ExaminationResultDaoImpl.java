package exam.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

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
				return er;
			}
		};
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
